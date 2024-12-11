package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.Global.Security.dto.GoogleResponse;
import com.daengdaeng_eodiga.project.Global.Security.dto.KakaoResponse;
import com.daengdaeng_eodiga.project.Global.Security.dto.OAuth2Response;
import com.daengdaeng_eodiga.project.Global.exception.DuplicateUserException;
import com.daengdaeng_eodiga.project.oauth.OauthProvider;
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
        OauthProvider provider=null;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
             provider= OauthProvider.kakao;

        } else if (registrationId.equals("google")) {
             oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
             provider= OauthProvider.google;
        }
        String email = oAuth2Response.getEmail();


        Optional<User> existData = userRepository.findByEmailAndOauthProvider(email,provider);
        if (existData.isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error(
                    "REDIRECT_TO_SIGNUP",
                    "REDIRECT_TO_SIGNUP: email=" + email + ", provider=" + provider,
                    null
            ));
        } else {
            if(existData.get().getDeletedAt()!=null){

                throw new OAuth2AuthenticationException(new OAuth2Error(
                    "DELETED_USER",
                    "DELETED_USER: email=" + email + ", provider=" + provider,
                    null
                ));
            }
            User user = existData.get();
            UserOauthDto userDTO = new UserOauthDto();
            userDTO.setEmail(user.getEmail());
            userDTO.setName(user.getEmail());
            userDTO.setProvider(user.getOauthProvider());
            return new CustomOAuth2User(userDTO);
        }
    }
}
