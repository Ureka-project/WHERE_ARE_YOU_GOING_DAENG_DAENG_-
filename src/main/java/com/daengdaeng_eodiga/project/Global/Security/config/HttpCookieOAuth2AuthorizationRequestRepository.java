package com.daengdaeng_eodiga.project.Global.Security.config;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Component

public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;
    private static final Logger logger = LoggerFactory.getLogger(HttpCookieOAuth2AuthorizationRequestRepository.class);


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // 로그 추가
        logger.info("Attempting to load OAuth2AuthorizationRequest from cookie");

        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = JWTUtil.getOauthCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> {
                    // 로그 추가
                    logger.info("Found OAuth2AuthorizationRequest cookie: {}", cookie);
                    return JWTUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
                })
                .orElse(null);

        // 로드된 OAuth2AuthorizationRequest 값이 null인지 확인하고 로그 추가
        if (oAuth2AuthorizationRequest == null) {
            logger.warn("OAuth2AuthorizationRequest not found in cookie or failed to deserialize");
        } else {
            logger.info("Successfully loaded OAuth2AuthorizationRequest: {}", oAuth2AuthorizationRequest);
        }

        return oAuth2AuthorizationRequest;
    }


    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        JWTUtil.addOauthCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, JWTUtil.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            JWTUtil.addOauthCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }


}