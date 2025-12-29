package com.documind.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ChunkService {

    private static final int CHUNK_SIZE = 500;  // 약 500 단어
    private static final int OVERLAP = 50;      // 청크 간 중복 50 단어

    /**
     * 텍스트를 청크로 분할
     */
    public List<String> splitIntoChunks(String text) {
        log.info("텍스트 청킹 시작: {} 글자", text.length());

        List<String> chunks = new ArrayList<>();

        // 공백 기준으로 단어 분리
        String[] words = text.split("\\s+");

        int start = 0;
        while (start < words.length) {
            int end = Math.min(start + CHUNK_SIZE, words.length);

            // 청크 생성
            StringBuilder chunk = new StringBuilder();
            for (int i = start; i < end; i++) {
                chunk.append(words[i]).append(" ");
            }

            chunks.add(chunk.toString().trim());

            // 다음 청크 시작 (overlap 적용)
            start += (CHUNK_SIZE - OVERLAP);
        }

        log.info("청킹 완료: {} 개 청크 생성", chunks.size());

        return chunks;
    }

    /**
     * 토큰 개수 추정 (대략적)
     */
    public int estimateTokenCount(String text) {
        // 간단한 추정: 단어 수 * 1.3
        int wordCount = text.split("\\s+").length;
        return (int) (wordCount * 1.3);
    }
}