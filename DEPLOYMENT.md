# 🚀 배포 가이드

## 📋 배포 환경별 설정

### 1. 로컬 개발 환경

```bash
# 기본 docker-compose.yml 사용
docker-compose up -d

# 접속: http://localhost:8080
```

### 2. AWS EC2 배포 (Nginx + Certbot)

```bash
# AWS용 docker-compose 파일 사용
docker-compose -f docker-compose.aws.yml up -d

# Certbot으로 SSL 인증서 발급 (최초 1회)
sudo certbot --nginx -d idea-brainstorm.duckdns.org

# 접속: https://idea-brainstorm.duckdns.org
```

**특징**:
- ✅ Nginx 컨테이너 포함
- ✅ 80, 443 포트 사용
- ✅ Certbot으로 SSL 관리

### 3. 개인 서버 배포 (Caddy)

```bash
# 서버에 Caddy 설치 (최초 1회)
sudo apt install caddy

# Caddyfile 설정
sudo vi /etc/caddy/Caddyfile
```

```caddyfile
idea-brainstorm.duckdns.org {
    reverse_proxy localhost:8080
}
```

```bash
# Caddy 재시작
sudo systemctl restart caddy

# 개인서버용 docker-compose 파일 사용
docker-compose -f docker-compose.homeserver.yml up -d

# 접속: https://idea-brainstorm.duckdns.org
```

**특징**:
- ✅ Caddy가 서버 레벨에서 관리
- ✅ 자동 HTTPS (설정 불필요!)
- ✅ Nginx 컨테이너 없음
- ✅ 8080 포트만 노출

## 🔄 환경 전환하기

### AWS → 개인 서버 이전

```bash
# 1. AWS에서 데이터 백업 (필요시)
docker exec brainstorm-mysql mysqldump -u root -p brainstorm > backup.sql

# 2. 개인 서버 환경 구축
ssh user@your-server
sudo apt install docker.io docker-compose caddy
sudo vi /etc/caddy/Caddyfile  # 설정 입력

# 3. 프로젝트 클론
git clone [저장소]
cd brainstorming-platform

# 4. .env 파일 설정
cp .env.example .env
vi .env  # API 키 등 입력

# 5. 배포
docker-compose -f docker-compose.homeserver.yml up -d

# 6. DuckDNS IP 변경
# https://www.duckdns.org/ 에서 IP를 개인 서버 공인 IP로 변경
```

## 📊 파일 구조 요약

```
brainstorming-platform/
├── docker-compose.yml              # 로컬 개발용
├── docker-compose.aws.yml          # AWS 배포용 (Nginx 포함)
├── docker-compose.homeserver.yml   # 개인서버 배포용 (Caddy)
├── Dockerfile.nginx                # AWS Nginx 이미지
├── nginx/
│   └── nginx.conf                  # AWS Nginx 설정
└── .env                            # 환경변수 (공통)
```

## 🔧 포트 설정

| 환경 | 웹 포트 | Spring Boot | Python | MySQL |
|------|---------|-------------|--------|-------|
| 로컬 | 8080 (직접) | 8080 | 8000 | 3306 |
| AWS | 80, 443 (Nginx) | 8080 | 8000 | 3306 |
| 개인서버 | 80, 443 (Caddy) | 8080 | 8000 | 3306 |

## ✅ 체크리스트

### AWS 배포시
- [ ] .env 파일 설정 완료
- [ ] `docker-compose.aws.yml` 사용
- [ ] Certbot으로 SSL 인증서 발급
- [ ] 443 포트 방화벽 오픈

### 개인서버 배포시
- [ ] Caddy 설치 및 설정
- [ ] Caddyfile에 도메인 추가
- [ ] .env 파일 설정 완료
- [ ] `docker-compose.homeserver.yml` 사용
- [ ] 공유기 포트포워딩 (80, 443)
- [ ] DuckDNS IP 주소 변경
