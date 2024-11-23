package com.daengdaeng_eodiga.project.Global.Geo.dto;


import lombok.Getter;

import java.util.List;
public class KakaoGeoApiDto {
    private List<Document> documents;

    // getters and setters
    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Getter
    public static class Document {
        private Object address;

    }
}