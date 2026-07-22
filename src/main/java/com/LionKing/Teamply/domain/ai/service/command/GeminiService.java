package com.LionKing.Teamply.domain.ai.service.command;

import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;
import com.LionKing.Teamply.domain.ai.exception.AiErrorCode;
import com.LionKing.Teamply.domain.ai.exception.AiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    private String callGeminiApi(String prompt) {
        try {
            String url = GEMINI_API_URL + geminiApiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(
                    Map.of("parts", List.of(
                            Map.of("text", prompt)
                    ))
            ));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Gemini API 호출 실패: {}", response.getStatusCode());
                throw new AiException(AiErrorCode.GEMINI_API_ERROR);
            }

            JsonNode root = objectMapper.readTree(response.getBody());
            String responseText = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText();

            // JSON 마크다운 포맷 제거 (있을 경우)
            return responseText.replaceAll("```json", "").replaceAll("```", "").trim();

        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            log.error("Gemini API 연동 중 HTTP 오류 발생: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Gemini API HTTP Error: " + e.getStatusCode() + ", Body: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Gemini API 연동 중 기타 오류 발생", e);
            throw new RuntimeException("Gemini API Error: " + e.getMessage(), e);
        }
    }

    public CollaborateManagerRes getCollaborateFeedback(String content) {
        String prompt = "다음 텍스트(팀 프로젝트 댓글이나 글)를 분석해서 모호한 점을 지적하고 어떻게 수정하면 좋을지 JSON 형식으로만 반환해줘.\n" +
                "형식: {\"feedbackPoints\": [\"지적사항1\", \"지적사항2\"], \"suggestions\": [\"추가하면 좋은정보1\"], \"suggestedText\": \"수정된 전체 예시 텍스트\"}\n" +
                "분석할 텍스트: " + content;

        String resultJson = callGeminiApi(prompt);
        try {
            return objectMapper.readValue(resultJson, CollaborateManagerRes.class);
        } catch (Exception e) {
            log.error("CollaborateManagerRes 파싱 실패", e);
            throw new AiException(AiErrorCode.GEMINI_API_ERROR);
        }
    }

    public MeetingMinutesRes getMeetingSummary(String meetingNotes) {
        String prompt = "다음 회의록이나 채팅 내역을 요약해서 JSON 형식으로만 반환해줘.\n" +
                "이때 해당 회의록을 통해 도출될 수 있는 향후 수행해야 할 '전체 작업 개수'도 추정해서 포함해줘.\n" +
                "형식: {\"meetingGoal\": \"회의 목적\", \"keyDiscussions\": [\"핵심논의1\"], \"decisions\": [\"결정사항1\"], \"totalTaskCount\": 5}\n" +
                "회의 내용: " + meetingNotes;

        String resultJson = callGeminiApi(prompt);
        try {
            return objectMapper.readValue(resultJson, MeetingMinutesRes.class);
        } catch (Exception e) {
            log.error("MeetingMinutesRes 파싱 실패", e);
            throw new AiException(AiErrorCode.GEMINI_API_ERROR);
        }
    }

    public ScheduleParseRes parseSchedules(String scheduleNotes) {
        String prompt = "다음 텍스트에서 캘린더에 등록할 일정들을 추출해서 JSON 형식으로만 반환해줘.\n" +
                "날짜는 'yyyy-MM-ddTHH:mm:ss' 형식이어야 해.\n" +
                "형식: {\"schedules\": [{\"title\": \"일정 제목\", \"startTime\": \"시작시간\", \"endTime\": \"종료시간\", \"memo\": \"기타 내용\"}]}\n" +
                "텍스트 내용: " + scheduleNotes;

        String resultJson = callGeminiApi(prompt);
        try {
            return objectMapper.readValue(resultJson, ScheduleParseRes.class);
        } catch (Exception e) {
            log.error("ScheduleParseRes 파싱 실패", e);
            throw new AiException(AiErrorCode.GEMINI_API_ERROR);
        }
    }
}
