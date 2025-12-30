package com.documind.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmbeddingService {

    private final WebClient webClient;

    public EmbeddingService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:5000")
                .build();
    }

    /**
     * 텍스트를 벡터로 변환
     */
    public List<Double> generateEmbedding(String text) {
        log.info("임베딩 생성 요청: {} 글자", text.length());

        Map<String, Object> response = webClient.post()
                .uri("/embed")
                .bodyValue(Map.of("text", text))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Double> embedding = (List<Double>) response.get("embedding");

        log.info("임베딩 생성 완료: {} 차원", embedding.size());

        return embedding;
    }

    /**
     * 벡터를 PostgreSQL vector 형식 문자열로 변환
     */
    public String embeddingToString(List<Double> embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.size(); i++) {
            sb.append(embedding.get(i));
            if (i < embedding.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}