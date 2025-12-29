package com.documind.backend.repository;

import com.documind.backend.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // 파일 타입으로 검색
    List<Document> findByFileType(String fileType);

    // 제목으로 검색 (부분 일치)
    List<Document> findByTitleContaining(String keyword);

    // 최신 문서 조회
    List<Document> findTop10ByOrderByCreatedAtDesc();
}