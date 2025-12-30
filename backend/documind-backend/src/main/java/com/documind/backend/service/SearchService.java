package com.documind.backend.service;

import com.documind.backend.domain.DocumentChunk;
import com.documind.backend.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository chunkRepository;
    private final GroqService groqService;

    /**
     * RAG 검색: 질문에 대한 답변 생성
     */
    public String search(String question) {
        log.info("검색 시작: {}", question);

        // 1. 질문을 벡터로 변환
        List<Double> questionEmbedding = embeddingService.generateEmbedding(question);
        String questionVector = embeddingService.embeddingToString(questionEmbedding);

        // 2. 유사한 청크 찾기 (상위 3개)
        List<DocumentChunk> similarChunks = chunkRepository.findSimilarChunks(questionVector, 3);

        if (similarChunks.isEmpty()) {
            return "관련 문서를 찾을 수 없습니다.";
        }

        log.info("유사한 청크 {}개 발견", similarChunks.size());

        // 3. 청크 텍스트 결합
        String context = similarChunks.stream()
                .map(DocumentChunk::getChunkText)
                .collect(Collectors.joining("\n\n---\n\n"));

        // 4. 프롬프트 생성
        String prompt = String.format("""
            다음 문서 내용을 참고하여 질문에 답변해주세요.
            
            문서 내용:
            %s
            
            질문: %s
            
            답변은 문서 내용을 바탕으로 정확하게 작성해주세요.
            핵심 3가지만 각각 1줄씩 간결하게 해줘
            만약 예시를 들 수 있는 내용이 있다면 예시도 만들어줘
            """, context, question);

        // 5. Groq API로 답변 생성
        String answer = groqService.chat(prompt);

        log.info("답변 생성 완료");

        return answer;
    }
}