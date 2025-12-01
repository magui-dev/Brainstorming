"""
FastAPI Dependencies
세션 검증, 공통 로직
"""

from fastapi import HTTPException
from typing import Dict


def get_session_or_404(session_id: str) -> Dict:
    """
    세션 존재 여부 확인 (Dependency)
    
    Args:
        session_id: 세션 ID
        
    Returns:
        Dict: 세션 데이터
        
    Raises:
        HTTPException: 세션을 찾을 수 없는 경우
    """
    # 여기서는 import를 함수 내부로 이동 (순환 참조 방지)
    import sys
    from pathlib import Path
    
    brainstorming_path = Path(__file__).resolve().parent.parent.parent.parent / "domain" / "brainstorming"
    sys.path.insert(0, str(brainstorming_path))
    
    from session_manager import SessionManager
    
    session_manager = SessionManager()
    session = session_manager.get_session(session_id)
    
    if not session:
        raise HTTPException(status_code=404, detail="세션을 찾을 수 없습니다.")
    
    return session


# 전역 session_manager (brainstorming.py에서 사용)
import sys
from pathlib import Path

brainstorming_path = Path(__file__).resolve().parent.parent.parent.parent / "domain" / "brainstorming"
sys.path.insert(0, str(brainstorming_path))

from session_manager import SessionManager
session_manager = SessionManager()
