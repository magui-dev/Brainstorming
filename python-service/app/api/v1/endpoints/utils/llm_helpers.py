"""
LLM 호출 헬퍼 함수
- Retry 로직
- 프롬프트 로딩
- 에러 처리
"""

from pathlib import Path
from typing import Optional, Dict, Any
from openai import OpenAI
import time
import logging

logger = logging.getLogger(__name__)


def load_prompt_template(name: str) -> str:
    """
    프롬프트 템플릿 파일 로드
    
    Args:
        name: 프롬프트 파일명 (확장자 제외)
        
    Returns:
        str: 프롬프트 템플릿 내용
    """
    prompts_dir = Path(__file__).parent.parent / "prompts"
    file_path = prompts_dir / f"{name}.txt"
    
    if not file_path.exists():
        raise FileNotFoundError(f"프롬프트 파일을 찾을 수 없습니다: {file_path}")
    
    return file_path.read_text(encoding="utf-8")


def call_llm_with_retry(
    client: OpenAI,
    model: str,
    messages: list,
    temperature: float = 0.7,
    max_tokens: int = 2000,
    max_retries: int = 3,
    **kwargs
) -> Optional[str]:
    """
    재시도 로직이 포함된 LLM 호출
    
    Args:
        client: OpenAI 클라이언트
        model: 모델 이름
        messages: 메시지 리스트
        temperature: 온도
        max_tokens: 최대 토큰
        max_retries: 최대 재시도 횟수
        **kwargs: 추가 파라미터
        
    Returns:
        str: LLM 응답 텍스트
        
    Raises:
        Exception: 모든 재시도 실패 시
    """
    last_error = None
    
    for attempt in range(max_retries):
        try:
            logger.info(f"LLM 호출 시도 {attempt + 1}/{max_retries}")
            
            response = client.chat.completions.create(
                model=model,
                messages=messages,
                temperature=temperature,
                max_tokens=max_tokens,
                **kwargs
            )
            
            content = response.choices[0].message.content.strip()
            
            if not content:
                raise ValueError("LLM 응답이 비어있습니다")
            
            logger.info(f"LLM 호출 성공 (시도 {attempt + 1})")
            return content
            
        except Exception as e:
            last_error = e
            logger.warning(f"LLM 호출 실패 (시도 {attempt + 1}/{max_retries}): {e}")
            
            if attempt < max_retries - 1:
                wait_time = (2 ** attempt) + 1  # Exponential backoff: 2, 3, 5초
                logger.info(f"{wait_time}초 후 재시도...")
                time.sleep(wait_time)
    
    # 모든 재시도 실패
    logger.error(f"모든 LLM 호출 시도 실패: {last_error}")
    raise Exception(f"LLM 호출 실패 (재시도 {max_retries}번): {last_error}")


def format_prompt(template: str, **kwargs) -> str:
    """
    프롬프트 템플릿 포맷팅
    
    Args:
        template: 프롬프트 템플릿
        **kwargs: 템플릿 변수
        
    Returns:
        str: 포맷팅된 프롬프트
    """
    try:
        return template.format(**kwargs)
    except KeyError as e:
        raise ValueError(f"프롬프트 템플릿에 필요한 변수가 없습니다: {e}")
