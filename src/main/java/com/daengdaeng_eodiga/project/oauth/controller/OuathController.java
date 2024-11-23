package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.oauth.service.OauthUserService;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
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



@RestController
@RequestMapping("/api/v1") // 경로를 명확히 지정
@RequiredArgsConstructor
public class OuathController {


    private final OauthUserService oauthUserService;
    private final JWTUtil jwtUtil;
    private  final RedisTokenRepository  redisTokenRepository;

    @GetMapping("/signup")
    public void showSignUpForm(@RequestParam String email, HttpServletResponse response) throws IOException {
        String targetUrl = "/signupPage.html";
        response.addHeader("email",email);
        response.sendRedirect(targetUrl);
    }

    @GetMapping("/loginSuccess")
    public void loginSuccess(HttpServletResponse response) throws IOException {
        response.sendRedirect("/loginSuccess.html");
    }
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@ModelAttribute SignUpForm signUpForm, HttpServletResponse response) {
        Map<String, String> responseMessage = new HashMap<>();
        oauthUserService.registerOrUpdateUser(signUpForm);
        String accessToken = jwtUtil.createJwt(signUpForm.getEmail(), 60 * 60 * 60L);
        String refreshToken = jwtUtil.createRefreshToken(signUpForm.getEmail(), 24 * 60 * 60 * 1000L);
        Cookie accessTokenCookie = jwtUtil.createCookie("Authorization", accessToken);
        response.addCookie(accessTokenCookie);
        redisTokenRepository.saveToken(refreshToken , 24 * 60 * 60 * 1000L,signUpForm.getEmail());
        response.addCookie(jwtUtil.createCookie("RefreshToken", refreshToken));
        return ResponseEntity.ok(responseMessage);
        }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@CookieValue("RefreshToken") String RefreshToken,
                                                      @AuthenticationPrincipal CustomOAuth2User principal,
                                                      HttpServletResponse response) {
        Map<String, String> responseMessage = new HashMap<>();
            UserOauthDto userOauthDto = principal.getUserDTO();


            Cookie RefreshCookie = jwtUtil.deletRefreshCookie("RefreshToken", null);
            Cookie accessCookie = jwtUtil.deletAcessCookie("Authorization", null);
            response.addCookie(RefreshCookie);
            response.addCookie(accessCookie);
            redisTokenRepository.deleteToken(RefreshToken);
            long expiration = jwtUtil.getExpiration(RefreshToken);
            if (expiration > 0) {
                redisTokenRepository.addToBlacklist(RefreshToken, expiration, userOauthDto.getEmail());
            }

        return ResponseEntity.ok(responseMessage);
    }



    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@CookieValue("RefreshToken") String RefreshToken,@AuthenticationPrincipal CustomOAuth2User customOAuth2User,HttpServletResponse response) {
        String userEmail = customOAuth2User.getEmail();

        if(oauthUserService.deleteUserByName(userEmail)) {
            // 현재 인증된 사용자 가져오기
            Cookie RefreshCookie = jwtUtil.deletRefreshCookie("RefreshToken", null);
            Cookie accessTokenCookie =jwtUtil.deletAcessCookie("Authorization", null);

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
    @GetMapping("/user/adjust")
    public ResponseEntity<Map<String, Object>> AdjustUserRequest(@AuthenticationPrincipal CustomOAuth2User principal) {
        UserOauthDto userOauthDto = principal.getUserDTO();
        UserDto userDto = oauthUserService.UserToDto(userOauthDto.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto); // 사용자 정보// HTML 링크
        return ResponseEntity.ok(response); // 200 OK 응답 반환
    }

    @PostMapping("/user/adjust")
    public ResponseEntity<String> AdjustUser(@ModelAttribute SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.registerOrUpdateUser(signUpForm);
        return ResponseEntity.ok("Success");

    }
    @GetMapping("/user/duplicateNicname")
    public ResponseEntity<Map<String, Boolean>> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = oauthUserService.isNicknameDuplicate(nickname);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(response);
    }
}
