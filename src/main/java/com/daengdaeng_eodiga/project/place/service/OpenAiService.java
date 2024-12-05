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
        try {
            String prompt = "다음 리뷰의 장점을 한 줄로 요약해 주세요:\n" + reviewContent;
            return openAiChatModel.call(prompt).trim();
        } catch (Exception e) {
            return "장점 요약을 생성할 수 없습니다.";
        }
    }

    public String summarizeCons(String reviewContent) {
        try {
            String prompt = "다음 리뷰의 단점을 한 줄로 요약해 주세요:\n" + reviewContent;
            return openAiChatModel.call(prompt).trim();
        } catch (Exception e) {
            return "단점 요약을 생성할 수 없습니다.";
        }
    }

}
