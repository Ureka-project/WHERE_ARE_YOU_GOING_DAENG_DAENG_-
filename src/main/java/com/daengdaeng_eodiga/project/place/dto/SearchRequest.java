package com.daengdaeng_eodiga.project.place.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    private String keyword;
    private Double latitude;
    private Double longitude;
    private int userId;
}
