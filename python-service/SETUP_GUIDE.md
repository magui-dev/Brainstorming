# 🚀 Python 브레인스토밍 API 설정 가이드

## 📦 1. Python 환경 설정

### 1-1. 가상환경 생성
```bash
cd python-service
python3 -m venv venv
```

### 1-2. 가상환경 활성화
```bash
# Mac/Linux
source venv/bin/activate

# Windows (Git Bash)
source venv/Scripts/activate

# Windows (CMD)
venv\Scripts\activate.bat
```

### 1-3. 의존성 설치
```bash
pip install -r requirements.txt
```

---

## 🔑 2. OpenAI API 키 설정

### .env 파일 수정:
```env
OPENAI_API_KEY=sk-proj-실제_API_키_입력
```

**API 키 받는 방법:**
1. https://platform.openai.com/api-keys 접속
2. "Create new secret key" 클릭
3. 생성된 키를 .env 파일에 붙여넣기

---

## 🚀 3. 서버 실행

### 방법 1: main.py 직접 실행
```bash
cd python-service
python main.py
```

### 방법 2: uvicorn 직접 실행
```bash
cd python-service
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

---

## ✅ 4. 동작 확인

### 브라우저에서 확인:
```
http://localhost:8000/health
```

**예상 응답:**
```json
{
  "status": "healthy",
  "service": "Brainstorming API",
  "openai_key_set": true
}
```

### Swagger 문서 확인:
```
http://localhost:8000/docs
```

---

## 🔧 5. 트러블슈팅

### 문제 1: 포트 충돌
```bash
# 다른 포트 사용
uvicorn main:app --port 8001
```

### 문제 2: OpenAI API 키 오류
```bash
# .env 파일 확인
cat .env | grep OPENAI_API_KEY
```

### 문제 3: ChromaDB 경로 오류
```bash
# 경로 확인
ls -la app/domain/brainstorming/data/chroma
```

### 문제 4: 모듈 import 오류
```bash
# 의존성 재설치
pip install --upgrade -r requirements.txt
```

---

## 📊 6. Spring Boot 연동 확인

**Python 서버 (8000) + Spring Boot 서버 (8080) 동시 실행 필요!**

### Java에서 테스트:
```java
RestTemplate restTemplate = new RestTemplate();
String url = "http://localhost:8000/health";
String response = restTemplate.getForObject(url, String.class);
System.out.println(response);
```

---

## 🎯 7. 파일 구조 확인

```
python-service/
├── main.py                 ✅ 생성됨
├── requirements.txt        ✅ 생성됨
├── .env                    ✅ 생성됨 (API 키 수정 필요!)
├── SETUP_GUIDE.md         ✅ 이 파일
├── venv/                  ⬜ pip install 후 생성됨
└── app/
    ├── __init__.py         ✅ 생성됨
    ├── api/
    │   ├── __init__.py     ✅ 생성됨
    │   └── v1/
    │       ├── __init__.py ✅ 생성됨
    │       └── endpoints/
    │           ├── __init__.py         ✅ 생성됨
    │           └── brainstorming.py    ✅ 복사됨
    └── domain/
        ├── __init__.py     ✅ 생성됨
        ├── common/
        │   ├── __init__.py             ✅ 생성됨
        │   └── base_session_manager.py ✅ 복사됨
        └── brainstorming/
            ├── __init__.py             ✅ 생성됨
            ├── session_manager.py      ✅ 복사됨
            ├── ephemeral_rag.py        ✅ 복사됨
            ├── domain_hints.py         ✅ 복사됨
            ├── service.py              ✅ 복사됨
            └── ... (기타 파일들)       ✅ 복사됨
```

---

## 🎉 8. 다음 단계

1. ✅ Python 환경 설정
2. ✅ OpenAI API 키 입력
3. ✅ 서버 실행
4. ✅ Health check 확인
5. ⏭️ Java Spring Boot 연동
6. ⏭️ 전체 워크플로우 테스트

---

**준비 완료되면 Java 코드 작성 시작!** 🚀
