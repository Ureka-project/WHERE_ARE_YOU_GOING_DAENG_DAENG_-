package com.daengdaeng_eodiga.project.Global.Security.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@AllArgsConstructor
public class OAuth2AuthorizationRequestDTO  implements Serializable {
    private String authorizationUri;
    private String clientId;
}
