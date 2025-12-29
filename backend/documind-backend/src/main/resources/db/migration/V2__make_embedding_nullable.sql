-- embedding 컬럼을 nullable로 변경
ALTER TABLE document_chunks ALTER COLUMN embedding DROP NOT NULL;