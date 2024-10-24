# AI-based TOPIK Question Generation and Mock Test CBT Platform

## 1. 프로젝트 소개

### 1.1 배경 및 필요성
한국어 능력시험(TOPIK)의 응시자 수는 매년 증가하고 있으며, 디지털 시험 환경의 필요성이 대두되고 있습니다. 이 프로젝트는 AI 기반 문제 생성 기술을 활용하여 TOPIK 시험 준비를 효율적으로 지원하고자 합니다. 기존 종이 기반의 시험 준비 방식에서 벗어나, 컴퓨터 기반 학습 및 모의시험을 제공함으로써 학습자의 편의와 학습 효과를 증대시키고자 합니다.

### 1.2 목표 및 주요 내용
프로젝트의 목표는 생성 AI 기술을 활용해 TOPIK 시험 문제를 자동으로 생성하고, 모의시험 CBT(Computer-Based Test) 플랫폼을 구현하는 것입니다. 이를 통해 학습자는 다음과 같은 기능을 활용할 수 있습니다:

- 다양한 유형의 문제 자동 생성
- 모의시험 CBT를 통한 실전 대비 학습
- 자동 채점 및 학습 통계 제공

## 2. 상세설계

### 2.1 시스템 구성도
![시스템 구성도](path_to_system_architecture_image)

### 2.2 사용 기술
- **Frontend**: React.js (Vite, Tailwind CSS)
- **Backend**: Spring (Java), PostgreSQL, FastAPI (Python)
- **AI Server**: GPT-4o, GPT-4-turbo, GPT-4-mini
- **Infrastructure**: AWS (EC2, S3, RDS, VPC), Docker, Redis

## 3. 설치 및 사용 방법

### 3.1 요구 사항
- Node.js v18 이상
- Python 3.9 이상
- Java 11 이상
- PostgreSQL 13 이상
- AWS 계정 및 설정

### 3.2 설치 방법

1. **백엔드**:
   ```bash
   cd backend
   ./gradlew build
   java -jar build/libs/backend.jar

2. **프론트엔드**:
   ```bash
   cd frontend
   npm install
   npm run dev

2. **AI 서버**:
   ```bash
   cd ai-server
   pip install -r requirements.txt
   uvicorn main:app --reload

### 3.3 사용 방법

1. `http://www.topiklingo.kro.kr/`으로 접속합니다.
2. 회원가입 후 로그인합니다.
3. 원하는 시험 유형을 선택하여 모의시험에 응시할 수 있습니다.
4. 응시 후 자동 채점 결과와 학습 통계를 확인할 수 있습니다.
5. 현재 학생 계정은 하루에 3번 시도가능하게 되어있습니다.

## 4. 소개 및 시연 영상
시연 영상 링크(YouTube): [https://www.youtube.com/watch?v=1234567890](https://www.youtube.com/watch?v=1234567890)

## 5. 팀 소개
- 김범수
    - Spring 개발 환경 구축
    - 메인 백엔드 및 API서버 개발
    - AWS기반 Infra Structure 구성 및 관리
    - 데이터베이스 구축
    - 서비스 간 통신 연결
    - 메인 프론트 개발
- 신병근
    - Python 개발 환경 구축
    - 서비스 아키텍처 설계
    - 데이터 구조 설계
    - TOPIK 문제 유형별 문제 생성 AI 연구 및 개발
    - 배포용 FastAPI 서버 구축
    - 문제 관련 프론트 개발
- 허진영:
    - Python 개발 환경 구축
    - 기존 기출 문제 크롤링 및 메타데이터 수집
    - 데이터 저장 및 정제 파이프라인 개발
    - TOPIK 문제 유형별 문제 생성 AI 연구
    - 쓰기 평가 모델 개발
- 지도교수 조준수
    - 기술 자문 및 프로젝트 지도

