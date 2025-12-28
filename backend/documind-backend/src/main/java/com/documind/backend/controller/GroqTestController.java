package com.documind.backend.controller;

import com.documind.backend.service.GroqApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Groq API 테스트용 컨트롤러
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class GroqTestController {

    private final GroqApiClient groqApiClient;

    /**
     * Groq API 연결 테스트
     */
    @GetMapping("/groq")
    public ResponseEntity<Map<String, Object>> testGroq() {
        boolean isConnected = groqApiClient.testConnection();

        return ResponseEntity.ok(Map.of(
                "service", "Groq API",
                "status", isConnected ? "connected" : "failed",
                "message", isConnected ?
                        "Groq API 연결 성공!" :
                        "Groq API 연결 실패. API 키를 확인하세요."
        ));
    }

    /**
     * 간단한 질문 테스트
     */
    @PostMapping("/groq/ask")
    public ResponseEntity<Map<String, String>> askQuestion(
            @RequestBody Map<String, String> request
    ) {
        String question = request.get("question");
        String context = request.getOrDefault("context", "일반 대화");

        String answer = groqApiClient.generateAnswer(question, context);

        return ResponseEntity.ok(Map.of(
                "question", question,
                "answer", answer
        ));
    }
}