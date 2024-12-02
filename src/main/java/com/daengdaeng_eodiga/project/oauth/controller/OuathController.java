package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.oauth.service.OauthUserService;
import com.daengdaeng_eodiga.project.oauth.service.TokenService;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import org.springframework.web.bind.annotation.*;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OuathController {
    //TODO : Map 대신 ResponseDto로  타입 변경 변경


    private final OauthUserService oauthUserService;
    private final TokenService tokenService;
    private final JWTUtil jwtUtil;
    private final RedisTokenRepository redisTokenRepository;

    @Value("${frontend.url}")
    private String frontUrl;

    @GetMapping("/signup")
    public void showSignUpForm(@RequestParam String email, @RequestParam String provider, HttpServletResponse response) throws IOException {

        ResponseCookie emailCookie = ResponseCookie.from("email", email)
            .path("/")
            .sameSite("Lax")
            .httpOnly(false)
            .secure(false)
            .maxAge(60 * 10)
            .domain(".daengdaeng-where.link")
            .build();
        response.addHeader("Set-Cookie", emailCookie.toString());

        ResponseCookie provideCookie = ResponseCookie.from("provider", provider)
            .path("/")
            .sameSite("Lax")
            .httpOnly(false)
            .secure(false)
            .maxAge(60 * 10)
            .domain(".daengdaeng-where.link")
            .build();
        response.addHeader("Set-Cookie", provideCookie.toString());
        response.sendRedirect(frontUrl+"/user-register?email="+email+"&provider=" + provider);
    }

    @GetMapping("/loginSuccess")
    public void loginSuccess(HttpServletResponse response) throws IOException {
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@Valid @RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.registerUser(signUpForm);
        return ResponseEntity.ok(ApiResponse.success(tokenService.generateTokensAndSetCookies(signUpForm.getEmail(), response)));
    }
    //Todo::@CookieValue("RefreshToken") String RefreshToken, 나중에 넣어야함
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal CustomOAuth2User principal,
                                         HttpServletResponse response) {
        UserOauthDto userOauthDto = principal.getUserDTO();
        return tokenService.deleteCookie(userOauthDto.getEmail(), null, response);
    }
    //Todo::@CookieValue("RefreshToken") String RefreshToken, 나중에 넣어야함
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(CustomOAuth2User customOAuth2User,
                                        HttpServletResponse response) {
        String userEmail = customOAuth2User != null ? customOAuth2User.getEmail() : null;
        oauthUserService.deleteUserByName(userEmail);
        return tokenService.deleteCookie(userEmail, null, response);
    }

    @GetMapping("/user/adjust")
    public ResponseEntity<ApiResponse<Map<String, Object>>> AdjustUserRequest(@AuthenticationPrincipal CustomOAuth2User principal) {
        UserOauthDto userOauthDto = principal.getUserDTO();
        UserDto userDto = oauthUserService.UserToDto(userOauthDto.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/user/adjust")
    public ResponseEntity<ApiResponse<?>> AdjustUser(@Valid @RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.AdjustUser(signUpForm);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    @GetMapping("/user/duplicateNickname")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkNicknameDuplicate( @RequestParam
                                                                                         String nickname) {
        boolean isDuplicate = oauthUserService.isNicknameDuplicate(nickname);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
