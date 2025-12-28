package com.documind.backend.service;

import com.documind.backend.config.GroqProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Groq API 클라이언트
 * OpenAI 호환 API를 사용하여 LLM 호출
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroqApiClient {

    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper;

    /**
     * Groq API를 호출하여 답변 생성
     *
     * @param prompt 사용자 질문
     * @param context 검색된 문서 컨텍스트
     * @return LLM 응답
     */
    public String generateAnswer(String prompt, String context) {
        log.debug("Groq API 호출 시작 - Model: {}", groqProperties.getModel());

        // 시스템 프롬프트 구성
        String systemPrompt = """
            당신은 기술 문서 전문가입니다.
            제공된 문서 내용을 바탕으로 정확하고 상세한 답변을 제공하세요.
            문서에 없는 내용은 추측하지 말고 "문서에서 해당 정보를 찾을 수 없습니다"라고 답변하세요.
            """;

        // 사용자 프롬프트 구성
        String userPrompt = String.format("""
            ### 참고 문서:
            %s
            
            ### 질문:
            %s
            
            ### 답변 지침:
            1. 문서 내용을 기반으로 답변하세요
            2. 구체적인 예시나 코드가 있다면 포함하세요
            3. 출처 문서를 명시하세요
            """, context, prompt);

        // 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "model", groqProperties.getModel(),
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.3,  // 창의성 낮춤 (정확성 우선)
                "max_tokens", 1024
        );

        try {
            // WebClient로 API 호출
            WebClient webClient = WebClient.builder()
                    .baseUrl(groqProperties.getBaseUrl())
                    .defaultHeader("Authorization", "Bearer " + groqProperties.getKey())
                    .defaultHeader("Content-Type", "application/json")
                    .build();

            String response = webClient.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(groqProperties.getTimeout()))
                    .block();

            // JSON 파싱하여 답변 추출
            JsonNode jsonNode = objectMapper.readTree(response);
            String answer = jsonNode.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            log.debug("Groq API 응답 완료 - 답변 길이: {}", answer.length());
            return answer;

        } catch (Exception e) {
            log.error("Groq API 호출 실패", e);
            throw new RuntimeException("LLM 답변 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * API 연결 테스트
     */
    public boolean testConnection() {
        try {
            String testAnswer = generateAnswer(
                    "Hello, please respond with 'OK'",
                    "Test document"
            );
            return testAnswer != null && !testAnswer.isEmpty();
        } catch (Exception e) {
            log.error("Groq API 연결 테스트 실패", e);
            return false;
        }
    }
}