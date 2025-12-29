package com.documind.backend.controller;

import com.documind.backend.domain.Document;
import com.documind.backend.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    /**
     * 문서 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title
    ) {
        try {
            log.info("문서 업로드 요청: {}", file.getOriginalFilename());

            Document document = documentService.uploadDocument(file, title);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "문서 업로드 성공",
                    "documentId", document.getId(),
                    "title", document.getTitle(),
                    "chunkCount", document.getChunks().size()
            ));

        } catch (Exception e) {
            log.error("문서 업로드 실패", e);

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "문서 업로드 실패: " + e.getMessage()
            ));
        }
    }
}