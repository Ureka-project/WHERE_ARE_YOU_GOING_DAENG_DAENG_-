package com.daengdaeng_eodiga.project.oauth.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.JWTUtil;
import com.daengdaeng_eodiga.project.oauth.dto.SignUpForm;
import com.daengdaeng_eodiga.project.oauth.service.OauthUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api") // 경로를 명확히 지정
@RequiredArgsConstructor
public class OuathController {


    private final OauthUserService oauthUserService;
    private final JWTUtil jwtUtil;

    @GetMapping("/signup")
    public ModelAndView  showSignUpForm(@RequestParam String email, Model model) {
        ModelAndView mav = new ModelAndView("signupPage");
        model.addAttribute("email", email);
        return mav;
    }

    @GetMapping("/loginSuccess")
    public ModelAndView  loginSuccess(Model model) {
        ModelAndView mav = new ModelAndView("loginSuccess");
        return mav;
    }
    @PostMapping("/signup")
    public Map<String, String> signup(@ModelAttribute SignUpForm signUpForm) {
        Map<String, String> response = new HashMap<>();
        try {

            String accessToken = oauthUserService.registerUser(signUpForm);  // 액세스 토큰
            String refreshToken = jwtUtil.createRefreshToken(signUpForm.getEmail(), "USER", 24 * 60 * 60 * 1000L);  // 리프레시 토큰
            Cookie accessTokenCookie = new Cookie("Authorization", accessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(60 * 60); // 1시간


        } catch (Exception e) {
            // 실패 처리
            response.put("error", "회원가입 중 문제가 발생했습니다.");
        }
        return response;
    }
    @PostMapping("/logout")
    public Map<String, String> logout(HttpServletResponse response) {
        Map<String, String> responseMessage = new HashMap<>();
        try {
            // 액세스 토큰 쿠키 삭제
            Cookie accessTokenCookie = new Cookie("Authorization", null);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(0); // 쿠키 삭제

            // 리프레시 토큰 쿠키 삭제
            Cookie refreshTokenCookie = new Cookie("RefreshToken", null);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(0); // 쿠키 삭제

            // 응답에 쿠키를 삭제하도록 설정
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);

            // 로그아웃 메시지 반환
            responseMessage.put("message", "로그아웃 되었습니다.");
        } catch (Exception e) {
            responseMessage.put("error", "로그아웃 중 문제가 발생했습니다.");
        }
        return responseMessage;
    }


}
