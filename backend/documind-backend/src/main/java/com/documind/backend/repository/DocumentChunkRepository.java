package com.documind.backend.repository;

import com.documind.backend.domain.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    // 특정 문서의 모든 청크 조회
    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(Long documentId);

    // 벡터 유사도 검색 (네이티브 쿼리)
    @Query(value = """
        SELECT * FROM document_chunks 
        ORDER BY embedding <-> CAST(:queryVector AS vector) 
        LIMIT :limit
        """, nativeQuery = true)
    List<DocumentChunk> findSimilarChunks(
            @Param("queryVector") String queryVector,
            @Param("limit") int limit
    );

    // 특정 문서의 청크 개수
    long countByDocumentId(Long documentId);

    @Modifying
    @Query(value = """
        UPDATE document_chunks 
        SET embedding = CAST(:embedding AS vector) 
        WHERE id = :chunkId
        """, nativeQuery = true)
    void updateEmbedding(@Param("chunkId") Long chunkId, @Param("embedding") String embedding);
}