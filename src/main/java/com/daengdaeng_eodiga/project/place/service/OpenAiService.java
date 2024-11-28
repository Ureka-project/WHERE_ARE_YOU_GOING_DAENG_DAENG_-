package com.daengdaeng_eodiga.project.place.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final OpenAiChatModel openAiChatModel;

    public OpenAiService(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    public String summarizePros(String reviewContent) {
        String prompt = "다음 리뷰의 장점을 한 줄로 요약해 주세요:\n" + reviewContent;
        return openAiChatModel.call(prompt).trim();
    }

    public String summarizeCons(String reviewContent) {
        String prompt = "다음 리뷰의 단점을 한 줄로 요약해 주세요:\n" + reviewContent;
        return openAiChatModel.call(prompt).trim();
    }
}
