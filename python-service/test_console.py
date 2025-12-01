"""
브레인스토밍 모듈 간단 테스트 (콘솔용)

Usage:
    conda activate brainstorm
    cd python-service
    python test_console.py
"""

import sys
from pathlib import Path

# 현재 파일 경로 기준으로 app 디렉토리 추가
current_dir = Path(__file__).parent
sys.path.insert(0, str(current_dir))

from app.domain.brainstorming.session_manager import SessionManager
from app.domain.brainstorming.ephemeral_rag import EphemeralRAG
import chromadb
from chromadb.config import Settings as ChromaSettings
from openai import OpenAI
from dotenv import load_dotenv
import os

# 환경변수 로드
load_dotenv()


def test_brainstorming():
    """브레인스토밍 전체 플로우 테스트"""
    
    print("\n" + "="*60)
    print("🧠 브레인스토밍 모듈 테스트 시작")
    print("="*60)
    
    # 1. 초기화
    print("\n[1단계] 초기화...")
    session_manager = SessionManager()
    openai_client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
    llm_model = os.getenv("LLM_MODEL", "gpt-4o")
    embedding_model = os.getenv("EMBEDDING_MODEL", "text-embedding-3-large")
    
    # ChromaDB 로드
    chroma_path = current_dir / "app" / "domain" / "brainstorming" / "data" / "chroma"
    chroma_client = chromadb.PersistentClient(
        path=str(chroma_path),
        settings=ChromaSettings(anonymized_telemetry=False)
    )
    
    try:
        permanent_collection = chroma_client.get_collection(name="brainstorming_techniques")
        print("✅ Permanent RAG 컬렉션 로드 완료")
    except Exception as e:
        print(f"⚠️  Permanent RAG 컬렉션 로드 실패: {e}")
        permanent_collection = None
    
    # 2. 세션 생성
    print("\n[2단계] 세션 생성...")
    session_id = session_manager.create_session()
    print(f"✅ 세션 ID: {session_id}")
    
    try:
        # 3. Q1: 목적 입력
        print("\n[3단계] Q1: 목적 입력...")
        purpose = "학생들을 위한 AI 기반 맞춤형 학습 앱 아이디어"
        session_manager.update_session(session_id, {'q1_purpose': purpose})
        print(f"✅ 목적: {purpose}")
        
        # 4. Q2: 워밍업 질문 생성 (선택사항, 생략 가능)
        print("\n[4단계] Q2: 워밍업 질문 생성 (생략)")
        
        # 5. Q3: 자유연상 입력
        print("\n[5단계] Q3: 자유연상 입력...")
        associations = ["학습", "AI", "맞춤형", "학생", "공부", "효율", "재미"]
        
        session = session_manager.get_session(session_id)
        ephemeral_rag = EphemeralRAG(
            session_id=session_id,
            collection_name=session['chroma_collection'],
            chroma_client=chroma_client
        )
        
        ephemeral_rag.add_associations(associations)
        session_manager.update_session(session_id, {
            'q3_associations': associations,
            'ephemeral_rag_initialized': True
        })
        print(f"✅ 자유연상: {associations}")
        
        # 6. 키워드 추출
        print("\n[6단계] Ephemeral RAG 키워드 추출...")
        keywords_data = ephemeral_rag.extract_keywords_by_similarity(
            purpose=purpose,
            top_k=5
        )
        extracted_keywords = [kw['keyword'] for kw in keywords_data]
        print(f"✅ 추출된 키워드: {extracted_keywords}")
        
        # 7. Permanent RAG 검색
        print("\n[7단계] Permanent RAG 검색...")
        rag_docs = []
        if permanent_collection:
            purpose_embedding = openai_client.embeddings.create(
                input=purpose,
                model=embedding_model
            ).data[0].embedding
            
            results = permanent_collection.query(
                query_embeddings=[purpose_embedding],
                n_results=3
            )
            
            if results and results.get('documents') and results['documents'][0]:
                rag_docs = results['documents'][0]
                print(f"✅ 검색된 브레인스토밍 기법: {len(rag_docs)}개")
                for i, doc in enumerate(rag_docs, 1):
                    print(f"   {i}. {doc[:100]}...")
        
        # 8. 아이디어 생성
        print("\n[8단계] 아이디어 생성 중... (LLM 호출)")
        
        rag_context = "\n\n".join(rag_docs) if rag_docs else ""
        
        prompt = f"""**역할**: 당신은 유능한 기획자입니다.

**목적**: "{purpose}"

**사용자의 연상 키워드**: {', '.join(extracted_keywords)}

**브레인스토밍 기법 참고**:
{rag_context}

**요구사항**:
1. 위 목적에 맞는 구체적인 아이디어를 2-3개 생성하세요.
2. 각 아이디어는 즉시 실행 가능하고 구체적이어야 합니다.
3. 간단한 SWOT 분석도 포함하세요.

**출력 형식**:
아이디어 1: [제목]
- 설명: [구체적인 실행 방법]
- SWOT: [간단한 분석]

아이디어 2: [제목]
- 설명: [구체적인 실행 방법]
- SWOT: [간단한 분석]
"""
        
        response = openai_client.chat.completions.create(
            model=llm_model,
            messages=[
                {"role": "system", "content": "당신은 유능한 기획자입니다."},
                {"role": "user", "content": prompt}
            ],
            temperature=0.7,
            max_tokens=1500
        )
        
        ideas_text = response.choices[0].message.content.strip()
        
        print("\n" + "="*60)
        print("🎉 생성된 아이디어:")
        print("="*60)
        print(ideas_text)
        
        print("\n" + "="*60)
        print("✅ 테스트 완료!")
        print("="*60)
        
    finally:
        # 9. 세션 정리
        print("\n[9단계] 세션 정리...")
        session_manager.delete_session(session_id)
        print("✅ 세션 삭제 완료")


if __name__ == "__main__":
    try:
        test_brainstorming()
    except Exception as e:
        print(f"\n❌ 에러 발생: {e}")
        import traceback
        traceback.print_exc()
