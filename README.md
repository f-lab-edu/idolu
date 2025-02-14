# I DOL U - Product

아이돌 굿즈 판매 이커머스 플랫폼 상품 서비스입니다.

## 실행 방법

### .env 파일 생성

```dotenv
# 예시
MYSQL_DATABASE=idolu-product
MYSQL_USERNAME=root
MYSQL_ROOT_PASSWORD=1234
TZ=Asia/Seoul
MYSQL_URL=r2dbc:mysql://localhost:3306/idolu-product
```
- docker-compose 실행을 위한 환경변수 설정

### docker-compose 실행

```bash
docker-compose --env-file .env up -d 
```

### 애플리케이션 실행

- SpringBoot 프로퍼티들도 같은 .env 파일의 환경변수를 사용
