# 📚 DocuMind - AI-Powered Document Search System

> Spring Boot + OpenAI GPT-4 + RAG 기반 기술 문서 검색 및 Q&A 시스템

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![AWS](https://img.shields.io/badge/AWS-Cloud-yellow.svg)](https://aws.amazon.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 🎯 프로젝트 개요

DocuMind는 기술 문서를 자연어로 검색하고 AI 기반 답변을 제공하는 RAG(Retrieval Augmented Generation) 시스템입니다.

### 주요 기능
- 📄 PDF/Markdown 문서 자동 파싱 및 임베딩
- 🔍 자연어 기반 의미론적 검색 (Semantic Search)
- 💬 GPT-4를 활용한 컨텍스트 기반 답변 생성
- 🗂️ 대화 히스토리 관리 및 세션 유지
- ☁️ AWS 클라우드 인프라 구축

## 🛠️ 기술 스택

### Backend
- Java 17, Spring Boot 3.2, Spring Data JPA
- PostgreSQL + pgvector (Vector Database)
- Redis (Caching & Session)
- LangChain4j (LLM Integration)

### Frontend
- React 18, TypeScript
- Redux Toolkit (State Management)
- Tailwind CSS

### AI/ML
- OpenAI GPT-4 API
- Vector Embeddings (text-embedding-ada-002)

### Infrastructure
- AWS (EC2, RDS, S3, Lambda, CloudFront)
- Docker & Docker Compose
- Terraform (IaC)
- GitHub Actions (CI/CD)

## 🏗️ 아키텍처
```
[Client] → [CloudFront] → [EC2/Spring Boot] → [RDS PostgreSQL + pgvector]
                              ↓                         ↓
                          [S3 Bucket]              [Redis Cache]
                              ↓
                          [Lambda] → [OpenAI API]
```

## 📋 프로젝트 진행 상황

- [x] 요구사항 정의
- [x] 기술 스택 선정
- [x] GitHub 저장소 생성
- [ ] AWS 인프라 설계
- [ ] 데이터베이스 스키마 설계
- [ ] Spring Boot 프로젝트 초기 세팅
- [ ] 문서 업로드 API 구현
- [ ] Vector DB 연동
- [ ] RAG 검색 엔진 구현
- [ ] React 프론트엔드 개발
- [ ] CI/CD 파이프라인 구축

## 🚀 로컬 실행 방법

### 사전 요구사항
- JDK 17 이상
- Docker & Docker Compose
- Node.js 18 이상
- PostgreSQL 15 (with pgvector)

### 실행
```bash
# Docker로 DB 실행
cd infrastructure/docker
docker-compose up -d

# Backend 실행
cd backend
./gradlew bootRun

# Frontend 실행 (추후)
cd frontend
npm install
npm start
```

## 📖 API 문서
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- API Docs: [docs/api-specs.md](docs/api-specs.md)

## 👤 개발자
**정민종**
- 경력: Java/Spring Boot 2년, React 2년
- 목표: AWS SAA 자격증 + 중견 IT기업 취업

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**개발 기간**: 2024.12 ~ 진행 중  
**포트폴리오 목적**: AWS 실무 역량 + 풀스택 개발 능력 증명