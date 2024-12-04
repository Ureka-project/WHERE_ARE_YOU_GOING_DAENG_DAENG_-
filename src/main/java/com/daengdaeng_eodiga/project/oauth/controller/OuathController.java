package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.daengdaeng_eodiga.project.Global.exception.UserFailedSaveException;
import com.daengdaeng_eodiga.project.Global.exception.UserNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.UserUnauthorizedException;
import com.daengdaeng_eodiga.project.oauth.OauthResult;
import com.daengdaeng_eodiga.project.oauth.dto.OauthResponse;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.oauth.service.OauthUserService;
import com.daengdaeng_eodiga.project.oauth.service.TokenService;
import com.daengdaeng_eodiga.project.user.dto.UserDto;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.registerUser(signUpForm);
        tokenService.generateTokensAndSetCookies(signUpForm.getEmail(), response);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue Cookie RefreshToken ,@AuthenticationPrincipal CustomOAuth2User principal,
                                         HttpServletResponse response) {
        String userEmail = principal.getUserDTO().getEmail();
        tokenService.deleteCookie(userEmail, response,RefreshToken);
        return  ResponseEntity.ok(ApiResponse.success(null));
    }
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@CookieValue Cookie RefreshToken ,@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                        HttpServletResponse response) {
        String userEmail = customOAuth2User.getUserDTO().getEmail();
        oauthUserService.deleteUserByName(userEmail);
        tokenService.deleteCookie(userEmail, response,RefreshToken);
        return  ResponseEntity.ok(ApiResponse.success(null));
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
    public ResponseEntity<ApiResponse<?>> AdjustUser(@AuthenticationPrincipal CustomOAuth2User principal ,
                                                     @Valid @RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.AdjustUser(signUpForm,principal.getUserDTO().getEmail());
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
