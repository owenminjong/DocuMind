package com.documind.backend.controller;

import com.documind.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * RAG 검색
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> search(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");

            log.info("검색 요청: {}", question);

            String answer = searchService.search(question);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "question", question,
                    "answer", answer
            ));

        } catch (Exception e) {
            log.error("검색 실패", e);

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "검색 실패: " + e.getMessage()
            ));
        }
    }
}