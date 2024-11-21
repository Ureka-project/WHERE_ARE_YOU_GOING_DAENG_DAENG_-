package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.oauth.service.OauthUserService;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil.deletAcessCookie;
import static com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil.deletRefreshCookie;


@RestController
@RequestMapping("/api") // 경로를 명확히 지정
@RequiredArgsConstructor
public class OuathController {


    private final OauthUserService oauthUserService;
    private final JWTUtil jwtUtil;
    private  final RedisTokenRepository  redisTokenRepository;
    private final UserRepository userRepository;


    @GetMapping("/signup")
    public void showSignUpForm(@RequestParam String email, HttpServletResponse response) throws IOException {
        String targetUrl = "/signupPage.html?email=" + email;
        response.sendRedirect(targetUrl);
    }

    @GetMapping("/loginSuccess")
    public void loginSuccess(HttpServletResponse response) throws IOException {
        response.sendRedirect("/loginSuccess.html");
    }
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@ModelAttribute SignUpForm signUpForm, HttpServletResponse response) {
        Map<String, String> responseMessage = new HashMap<>();
        try {
            oauthUserService.registerUser(signUpForm);  // 회원가입 처리
            // JWT 토큰 발급
            String accessToken = jwtUtil.createJwt(signUpForm.getNickname(),"USER",signUpForm.getEmail(), 60 * 60 * 60L);
            String refreshToken = jwtUtil.createRefreshToken(signUpForm.getNickname(), "USER",signUpForm.getEmail(), 24 * 60 * 60 * 1000L);

            // 토큰을 쿠키에 담기
            Cookie accessTokenCookie = JWTUtil.createCookie("Authorization", accessToken); // 액세스 토큰 쿠키
            response.addCookie(accessTokenCookie);
            redisTokenRepository.saveToken(refreshToken , 24 * 60 * 60 * 1000L,signUpForm.getEmail());

            // 회원가입 성공 후 리다이렉트 URL 반환
            response.addCookie(JWTUtil.createCookie("RefreshToken", refreshToken));
            responseMessage.put("redirectUrl", "/loginSuccess.html");
            return ResponseEntity.ok(responseMessage);  // JSON으로 반환

        } catch (IllegalArgumentException e) {
            // 중복된 닉네임이나 이메일에 대한 처리
            responseMessage.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMessage);
        } catch (Exception e) {
            // 그 외 예외 처리
            responseMessage.put("error", "회원가입 중 문제가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMessage);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@CookieValue("RefreshToken") String RefreshToken,
                                                      @AuthenticationPrincipal CustomOAuth2User principal,
                                                      HttpServletResponse response) {
        Map<String, String> responseMessage = new HashMap<>();

        try {
            UserOauthDto userOauthDto = principal.getUserDTO();

            // 블랙리스트 처리
            Cookie RefreshCookie = deletRefreshCookie("RefreshToken", null);
            Cookie accessCookie = deletAcessCookie("Authorization", null);
            response.addCookie(RefreshCookie);
            response.addCookie(accessCookie);
            redisTokenRepository.deleteToken(RefreshToken);
            long expiration = jwtUtil.getExpiration(RefreshToken);
            System.out.println(expiration);
            if (expiration > 0) {
                redisTokenRepository.addToBlacklist(RefreshToken, expiration, userOauthDto.getEmail());
            }
            System.out.println("ww");
            responseMessage.put("message", "로그아웃 성공.");
        } catch (Exception e) {
            responseMessage.put("error", "로그아웃 중 에러 발생.");
        }

        return ResponseEntity.ok(responseMessage);
    }



    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUser(@CookieValue("RefreshToken") String RefreshToken,@AuthenticationPrincipal CustomOAuth2User customOAuth2User,HttpServletResponse response) {
        String userEmail = customOAuth2User.getEmail();
        System.out.println(userEmail);
        if(oauthUserService.deleteUserByName(userEmail)) {
            // 현재 인증된 사용자 가져오기
            Cookie RefreshCookie = deletRefreshCookie("RefreshToken", null);
            Cookie accessTokenCookie =deletAcessCookie("Authorization", null);

            response.addCookie(accessTokenCookie);
            response.addCookie(RefreshCookie);
            redisTokenRepository.deleteToken(RefreshToken);
            return ResponseEntity.ok("{\"message\":\"User deleted successfully\"}");
        }
        else
        {
            return ResponseEntity.ok("User deleted failed");
        }

    }
}
