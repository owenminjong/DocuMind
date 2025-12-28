-- pgvector extension 활성화
CREATE EXTENSION IF NOT EXISTS vector;

-- 1. documents 테이블
CREATE TABLE documents (
                           id BIGSERIAL PRIMARY KEY,
                           title VARCHAR(255) NOT NULL,
                           file_name VARCHAR(255) NOT NULL,
                           file_type VARCHAR(10) NOT NULL,
                           file_size BIGINT,
                           content TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. document_chunks 테이블
CREATE TABLE document_chunks (
                                 id BIGSERIAL PRIMARY KEY,
                                 document_id BIGINT NOT NULL,
                                 chunk_text TEXT NOT NULL,
                                 chunk_index INTEGER NOT NULL,
                                 token_count INTEGER,
                                 embedding vector(384),
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE,
                                 UNIQUE (document_id, chunk_index)
);

-- 벡터 검색용 인덱스
CREATE INDEX idx_chunk_embedding
    ON document_chunks USING hnsw (embedding vector_cosine_ops);

-- document_id 검색 최적화
CREATE INDEX idx_chunk_document_id ON document_chunks(document_id);