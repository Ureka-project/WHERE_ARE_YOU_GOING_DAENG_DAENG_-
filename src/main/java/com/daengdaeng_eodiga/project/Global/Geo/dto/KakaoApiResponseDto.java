package com.daengdaeng_eodiga.project.Global.Geo.dto;

import com.nimbusds.openid.connect.sdk.claims.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class KakaoApiResponseDto {
    // Getter 및 Setter
    private List<Document> documents; // "documents" 필드는 List<Document> 타입

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Getter
    public static class Document {
        private Address address;  // 추가된 필드
        private String addressName;
        private String addressType;
        @Setter
        private String x;
        @Setter
        private String y;

    }
}
