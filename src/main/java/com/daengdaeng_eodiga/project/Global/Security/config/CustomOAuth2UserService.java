package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Security.dto.GoogleResponse;
import com.daengdaeng_eodiga.project.Global.Security.dto.KakaoResponse;
import com.daengdaeng_eodiga.project.Global.Security.dto.OAuth2Response;
import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import com.daengdaeng_eodiga.project.user.repository.UserRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Unsupported OAuth provider");
        }

        String email = oAuth2Response.getEmail();
        if (email == null || email.isEmpty()) {
            throw new OAuth2AuthenticationException("Principal name (email) cannot be empty");
        }

        Optional<User> existData = userRepository.findByEmail(email);

        if (!existData.isPresent()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("REDIRECT_TO_SIGNUP", "REDIRECT_TO_SIGNUP: " + email, null));

        } else {
            User user = existData.get();
            UserOauthDto userDTO = new UserOauthDto();
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getEmail());
            return new CustomOAuth2User(userDTO);
        }
    }
}