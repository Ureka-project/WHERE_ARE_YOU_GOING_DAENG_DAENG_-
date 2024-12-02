package com.daengdaeng_eodiga.project.Global.Geo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimbusds.openid.connect.sdk.claims.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoApiResponseDto {
    private List<Document> documents;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("address_type")
        private String addressType;

        private String x;
        private String y;

        @JsonProperty("address")
        private Address address;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            @JsonProperty("region_1depth_name")
            private String region1DepthName;

            @JsonProperty("region_2depth_name")
            private String region2DepthName;

            @JsonProperty("region_3depth_name")
            private String region3DepthName;
        }
    }
}
