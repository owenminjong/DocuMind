from fastapi import FastAPI
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
from typing import List

app = FastAPI()

# 모델 로드 (최초 1회, 약 90MB)
model = SentenceTransformer('sentence-transformers/all-MiniLM-L6-v2')

class EmbeddingRequest(BaseModel):
    text: str

class EmbeddingResponse(BaseModel):
    embedding: List[float]

@app.get("/")
def root():
    return {"message": "Embedding Service is running!"}

@app.post("/embed", response_model=EmbeddingResponse)
def create_embedding(request: EmbeddingRequest):
    """텍스트를 벡터로 변환"""
    embedding = model.encode(request.text)
    return {"embedding": embedding.tolist()}

@app.post("/embed/batch")
def create_embeddings_batch(texts: List[str]):
    """여러 텍스트를 한 번에 벡터로 변환"""
    embeddings = model.encode(texts)
    return {"embeddings": [emb.tolist() for emb in embeddings]}