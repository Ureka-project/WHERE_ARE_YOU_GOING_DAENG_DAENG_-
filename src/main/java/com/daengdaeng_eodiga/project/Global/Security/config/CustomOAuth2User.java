package com.daengdaeng_eodiga.project.Global.Security.config;

import com.daengdaeng_eodiga.project.oauth.dto.UserOauthDto;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
@Data
public class CustomOAuth2User implements OAuth2User {

    private final UserOauthDto userDTO;

    public CustomOAuth2User(UserOauthDto userDTO) {

        this.userDTO = userDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDTO.getRole();
            }
        });

        return collection;
    }


    public String getEmail() {

        return userDTO.getEmail();
    }

    @Override
    public String getName() {
        return userDTO.getName();
    }


}