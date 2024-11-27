package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.Global.Redis.Repository.RedisTokenRepository;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
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
    private final TokenService tokenService;

    @GetMapping("/signup")
    public ResponseEntity<ApiResponse<OauthResponse>> showSignUpForm(@RequestParam String email, HttpServletResponse response) throws IOException {
        OauthResponse oauthResponse = new OauthResponse(email, OauthResult.NEED_SIGNUP);
        return ResponseEntity.ok(ApiResponse.success(oauthResponse));
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<ApiResponse<?>> loginSuccess(HttpServletResponse response) throws IOException {
        OauthResponse oauthResponse = new OauthResponse(null, OauthResult.LOGIN_SUCCESS);
        return ResponseEntity.ok(ApiResponse.success(oauthResponse));
    }
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.registerOrUpdateUser(signUpForm);
        return tokenService.generateTokensAndSetCookies(signUpForm.getEmail(), response);
        }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("RefreshToken") String RefreshToken,
                                         @AuthenticationPrincipal CustomOAuth2User principal,
                                         HttpServletResponse response) {
        UserOauthDto userOauthDto = principal.getUserDTO();
        return tokenService.deleteCookie(userOauthDto.getEmail(), RefreshToken, response);
    }
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@CookieValue("RefreshToken") String RefreshToken,
                                        @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                        HttpServletResponse response) {
        String userEmail = customOAuth2User != null ? customOAuth2User.getEmail() : null;
        if (userEmail == null || userEmail.isEmpty()) {
            throw new UserUnauthorizedException();
        }
        oauthUserService.deleteUserByName(userEmail);
        return tokenService.deleteCookie(userEmail, RefreshToken, response);
    }

    @GetMapping("/user/adjust")
    public ResponseEntity<ApiResponse<Map<String, Object>>> AdjustUserRequest(@AuthenticationPrincipal CustomOAuth2User principal) {
        UserOauthDto userOauthDto = principal.getUserDTO();
        UserDto userDto = oauthUserService.UserToDto(userOauthDto.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/user/adjust")
    public ResponseEntity<ApiResponse<?>> AdjustUser(@RequestBody SignUpForm signUpForm, HttpServletResponse response) {
        oauthUserService.registerOrUpdateUser(signUpForm);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @GetMapping("/user/duplicateNicname")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = oauthUserService.isNicknameDuplicate(nickname);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
