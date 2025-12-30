package com.documind.backend.service;

import com.documind.backend.domain.Document;
import com.documind.backend.domain.DocumentChunk;
import com.documind.backend.repository.DocumentChunkRepository;
import com.documind.backend.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository chunkRepository;
    private final DocumentParserService parserService;
    private final ChunkService chunkService;
    private final EmbeddingService embeddingService;

    /**
     * 문서 업로드 및 저장
     */
    @Transactional
    public Document uploadDocument(MultipartFile file, String title) throws IOException {
        log.info("문서 업로드 시작: {}", file.getOriginalFilename());

        // 1. 파일 타입 확인
        String fileType = getFileType(file.getOriginalFilename());

        // 2. 텍스트 추출
        String content = parserService.parse(file, fileType);

        // 3. Document 엔티티 생성 및 저장
        Document document = Document.builder()
                .title(title != null ? title : file.getOriginalFilename())
                .fileName(file.getOriginalFilename())
                .fileType(fileType)
                .fileSize(file.getSize())
                .content(content)
                .build();

        document = documentRepository.save(document);
        log.info("문서 저장 완료: ID={}", document.getId());

        // 4. 텍스트를 청크로 분할
        List<String> chunks = chunkService.splitIntoChunks(content);

        // 5. 청크 저장
        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);

            DocumentChunk chunk = DocumentChunk.builder()
                    .document(document)
                    .chunkText(chunkText)
                    .chunkIndex(i)
                    .tokenCount(chunkService.estimateTokenCount(chunkText))
                    //.embedding(null)
                    .build();

            chunkRepository.save(chunk);

            // 6. 임베딩 생성 및 업데이트
            try {
                List<Double> embedding = embeddingService.generateEmbedding(chunkText);
                String embeddingStr = embeddingService.embeddingToString(embedding);
                chunkRepository.updateEmbedding(chunk.getId(), embeddingStr);

                log.info("청크 {} 임베딩 완료", i);
            } catch (Exception e) {
                log.error("청크 {} 임베딩 실패: {}", i, e.getMessage());
            }
        }

        log.info("청크 저장 완료: {} 개", chunks.size());

        return document;
    }

    /**
     * 파일 확장자로 타입 추출
     */
    private String getFileType(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다: " + filename);
        }

        String extension = filename.substring(lastDot + 1).toUpperCase();

        return switch (extension) {
            case "PDF" -> "PDF";
            case "MD", "MARKDOWN" -> "MD";
            case "TXT" -> "TXT";
            default -> throw new IllegalArgumentException("지원하지 않는 파일 타입: " + extension);
        };
    }
}