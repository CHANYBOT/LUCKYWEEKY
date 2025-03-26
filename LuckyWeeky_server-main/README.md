# 🗓LuckyWeeky: 음성을 통한 AI 일정 계획 캘린더 서비스

---
<div align="center">
    <img src="https://github.com/user-attachments/assets/1e304651-bdd5-43ab-a884-79fede7964b1" alt="luckyweekylogo2" width="200">
    <img src="https://github.com/user-attachments/assets/37b49949-9df3-4c2a-ad99-3812e593c1f6" alt="screenshot" width="800">
</div>

> **삼성 청년 소프트웨어 아카데미 12기**  
> 개발기간 : 3주 (2024.11.04 ~ 11.26)

---

## 배포주소
[https://luckyweeky.store](https://luckyweeky.store)

---
## 팀 소개

| 정지안 [PL]                                                                 | 우성문                                                                 |
|-----------------------------------------------------------------------------|------------------------------------------------------------------------|
| <p align="center">![jja](https://github.com/user-attachments/assets/5a385ccc-7b90-40fb-8624-0d490994dc33)</p> | <p align="center">![wsm](https://github.com/user-attachments/assets/513c772f-e074-462b-8132-a5dcad947b31)</p> |
| <p align="center">[@jja6312](https://github.com/jja6312)</p>                | <p align="center">[@tjdansw](https://github.com/tjdansw)</p>           |
| <p align="center">FE, Infra, BE (AI 일정등록/음성 일정등록), APM</p>              | <p align="center">BE (서블릿 환경 구축/로그인/회원가입/일정 CRUD)</p>  |

---

## 프로젝트 소개

**음성 혹은 텍스트를 통해 할 일을 입력하면, 주간 계획을 AI로 제안받는 서비스입니다.**

기존 캘린더 서비스에서 사용자는 특정 목표 달성을 위해 단계별 일정을 직접 고민하고 수동으로 입력해야 한다는 번거로움이 있었습니다.  
**LuckyWeeky**는 **생성형 AI**와 **음성 인식** 기능을 결합하여 **개인화된 주간 일정**을 빠르게 추천함으로써,  
사용자의 시간을 절약하고 **음성 명령**으로 편리하게 계획을 등록할 수 있도록 지원합니다.


## 주안점
- 스프링의 소스코드를 확인해보며, 개발시 대다수의 기능을 사용하지 않는다는 사실에서 성능에 대한 의문을 가졌음.
이에 웹 서칭을 진행했고, Servlet을 사용하면 4~6배의 성능 이점을 가질수 있다는 InfoQ 사이트의 비교 표를 근거로 Servlet을 사용하기로 결정했습니다.
- 3주 간 프로젝트를 진행하며 새로 배운 기술 : Docker, Terraform, Prometheus, Grafana, AWS ECS Fargate, https통신, AWS NAT, 다중AZ를 통한 로드밸런싱 등 필요 기술을 적극적으로 학습 및 적용
---

# 시작 가이드

## 환경
- **JDK 17+**
- **Jakarta EE 10**
- **Tomcat 10.1+**
- **MySQL 8.x**
- **Node.js 16+** (프론트엔드용)

## 설치

설치 전 주의사항: **AWS 계정**, **Naver Cloud Platform 계정**, **OpenAI 계정**이 필요합니다.

```bash
# 1. 프로젝트 클론 및 빌드
$ git clone https://github.com/jja6312/LuckyWeeky_server.git
$ cd LuckyWeeky_server
$ mvn clean package

# 2. Tomcat 배포
$ cp target/ROOT.war /path/to/tomcat/webapps/
$ /path/to/tomcat/bin/startup.sh  # 톰캣 실행

# 3. MySQL DB 설정
CREATE DATABASE luckyweeky CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- (1) 사용자 테이블
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    oauth_provider VARCHAR(50),
    oauth_id VARCHAR(255),
    birth_date DATE,
    profile_image_key VARCHAR(255),
    last_login_at DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- (2) 사용자 토큰 테이블
CREATE TABLE usertoken (
    user_id BIGINT PRIMARY KEY,
    token VARCHAR(1000) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (3) 사용자 솔트 테이블
CREATE TABLE usersalt (
    user_id BIGINT PRIMARY KEY,
    salt VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (4) 메인 일정 테이블
CREATE TABLE mainschedule (
    main_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    color CHAR(7),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- (5) 서브 일정 테이블
CREATE TABLE subschedule (
    sub_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    main_schedule_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    is_completed TINYINT(1) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (main_schedule_id) REFERENCES mainschedule(main_schedule_id) ON DELETE CASCADE
);

# 4. src/main/webapp/WEB-INF/local-secrets.json 파일 생성 및 환경 변수 적용
{
  "AWS_ACCESS_KEY": "YOUR_AWS_ACCESS_KEY",
  "AWS_SECRET_KEY": "YOUR_AWS_SECRET_KEY",
  "BUCKET_NAME": "YOUR_S3_BUCKET_NAME",
  "DB_URL": "jdbc:mysql://localhost:3306/luckyweekydb",
  "DB_USERNAME": "root",
  "DB_PASSWORD": "1234",
  "MACHINE_ID": "1",
  "OPENAI_API_KEY": "YOUR_OPENAI_API_KEY",
  "GPT_API_ENDPOINT": "https://api.openai.com/v1/chat/completions",
  "CLOVA_API_URL": "https://naveropenapi.apigw.ntruss.com/recog/v1/stt",
  "CLOVA_ACCESS_KEY": "YOUR_CLOVA_ACCESS_KEY",
  "CLOVA_SECRET_KEY": "YOUR_CLOVA_SECRET_KEY",
  "SECREATKEY": "pdFfOvKx3mEmfhkZcTWd5WNCaHxeInRK"
}
```
# 기술 스택🐈

| Category | Technologies                                                                                                                     |
|----------|----------------------------------------------------------------------------------------------------------------------------------|
| **FE**   | [![React Badge](https://img.shields.io/badge/React-61DAFB.svg?style=flat&logo=react&logoColor=white)]() [![TailwindCSS Badge](https://img.shields.io/badge/Tailwind_CSS-38B2AC.svg?style=flat&logo=Tailwind%20CSS&logoColor=white)]() [![Zustand Badge](https://img.shields.io/badge/Zustand-8AA3FF.svg?style=flat&logo=Zustand&logoColor=white)]() |
| **BE**   | [![Servlet Badge](https://img.shields.io/badge/Servlet-EB7A07.svg?style=flat&logo=openjdk&logoColor=white)]() [![MyBatis Badge](https://img.shields.io/badge/MyBatis-B32137.svg?style=flat&logo=data:image/svg+xml;base64,PHN2ZyBoZWlnaHQ9IjMwMCIgdmlld0JveD0iMCAwIDI2NSAzMDAiIHdpZHRoPSIyNjUiIHhtbDpzcGFjZT0icHJlc2VydmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PC9zdmc+&logoColor=white)]() [![Docker Badge](https://img.shields.io/badge/Docker-2496ED.svg?style=flat&logo=Docker&logoColor=white)]() [![Tomcat Badge](https://img.shields.io/badge/Tomcat_10.1.3-F8DC75.svg?style=flat&logo=apache-tomcat&logoColor=black)]() |
| **DB**   | [![MySQL Badge](https://img.shields.io/badge/MySQL-4479A1.svg?style=flat&logo=mysql&logoColor=white)]()                                                               |
| **Infra**| [![AWS Badge](https://img.shields.io/badge/AWS-232F3E.svg?style=flat&logo=amazon-aws&logoColor=white)]() [![Terraform Badge](https://img.shields.io/badge/Terraform-7B42BC.svg?style=flat&logo=terraform&logoColor=white)]() |
| **APM**  | [![Prometheus Badge](https://img.shields.io/badge/Prometheus-E6522C.svg?style=flat&logo=prometheus&logoColor=white)]() [![Grafana Badge](https://img.shields.io/badge/Grafana-F46800.svg?style=flat&logo=grafana&logoColor=white)]() [![JMeter Badge](https://img.shields.io/badge/JMeter-D22128.svg?style=flat&logo=apache-jmeter&logoColor=white)]() |



---

# 화면 구성 📺

## 대표 이미지
![대표이미지](https://github.com/user-attachments/assets/7a951c4b-161b-4da3-af2d-e3ca59fa3e84)

| 기능명         | 이미지                                                                                     | 기능명         | 이미지                                                                                     |
|----------------|--------------------------------------------------------------------------------------------|----------------|--------------------------------------------------------------------------------------------|
| 1. 로그인      | ![0로그인](https://github.com/user-attachments/assets/cc41194c-c4be-498a-b766-afed77562f24) | 2. 회원가입    | ![1회원가입](https://github.com/user-attachments/assets/b3e2926a-3c91-48fd-925d-ca9afaa1cad9) |
| 3. 메인화면    | ![2메인화면](https://github.com/user-attachments/assets/aa4432c2-1e7d-42d2-8c02-ea9b1f2880dc) | 4. 일정생성    | ![3_1일정생성](https://github.com/user-attachments/assets/2de4fc89-a1d6-49ef-9b86-744cc7ec4e52) |
| 5. 일정조회    | ![3_2일정조회](https://github.com/user-attachments/assets/808b4f19-0d82-4b47-b712-cc1790b8c4eb) | 6. 일정수정    | ![3_3일정수정png](https://github.com/user-attachments/assets/50688d61-7cb3-4963-82de-0f496b7d3ec5) |
| 7. AI 일정생성 | ![4_1AI일정생성](https://github.com/user-attachments/assets/3b65c370-0bce-4786-9422-2b9c0b71ac3d) | 8. AI 완료    | ![4_2AI일정생성완료](https://github.com/user-attachments/assets/db1ddcb2-aedb-4437-887c-44a74576e04a) |
| 9. AI 수정     | ![4_3AI일정수정](https://github.com/user-attachments/assets/9d6b12d4-96cc-420f-a875-57f3725332b0) | 10. 재요청     | ![4_4AI일정재요청](https://github.com/user-attachments/assets/ee477a06-ea95-4b6b-bdfd-bb9526f04bb6) |
| 11. 재요청 완료 | ![4_5AI일정재요청완료](https://github.com/user-attachments/assets/8f1963bb-95c8-4507-9a55-82ad19fd8989) |                |                                                                                            |


---

# 주요 기능 📦

⭐️ **AI 기반 일정 생성**  
  - 음성 또는 텍스트로 목표를 입력하면, AI가 구체적이고 전문적인 일정을 제안합니다.
  - 제안 받은 AI 일정에 대해 보완할 부분을 텍스트로 재요청하거나, 반영할 수 있습니다.

---

# 아키텍처

## 클라우드 아키텍처 ☁
![real](https://github.com/user-attachments/assets/be4b7237-9db8-468a-861d-36d939ef751f)

## UML: 유스케이스 다이어그램
![usecase drawio](https://github.com/user-attachments/assets/d4ae0a71-f1d2-4288-a432-785fc1abec87)

## UML: 시퀀스 다이어그램
| 기능명         | 이미지                                                                                     | 기능명         | 이미지                                                                                     |
|----------------|--------------------------------------------------------------------------------------------|----------------|--------------------------------------------------------------------------------------------|
| 1. 로그인      | ![1로그인](https://github.com/user-attachments/assets/c5fc5ee8-c210-4833-a872-c9a274332045) | 2. 회원가입    | ![5회원가입](https://github.com/user-attachments/assets/57d0f1c4-ad80-49d1-b445-3ba2d27e9f75) |
| 3. 일정등록    | ![3일반일정등록](https://github.com/user-attachments/assets/d8850174-1faf-41f0-9d91-74e1ea753d03) | 4. AI 일정등록    | ![2AI일정등록](https://github.com/user-attachments/assets/d819866f-1a9a-452d-93a3-b358d6d3638c) |
| 5. 일정 수정 삭제    | ![4일정수정삭제](https://github.com/user-attachments/assets/cad6b13c-93fd-4365-a268-0dde271676a1) |   | |

## UML: 플로우 차트
<img width="5213" alt="FlowChart" src="https://github.com/user-attachments/assets/f4d16a1c-0589-427d-b3a4-cc6cf94366c6" />

## ERD
![erd](https://github.com/user-attachments/assets/d8bf25d1-1271-445a-9f09-760e486533cf)

---

## 디렉토리 구조

```bash
LuckyWeeky_server/
├── Dockerfile                 # Docker 설정 파일
├── ecs-task-definition.json   # AWS ECS 태스크 정의 파일
├── pom.xml                    # Maven 프로젝트 설정 파일
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/ssafy/luckyweeky/
│   │   │       ├── common/            # 공통 모듈 (DispatcherServlet, 필터, 유틸리티)
│   │   │       │   ├── DispatcherServlet.java   # 커스텀 DispatcherServlet
│   │   │       │   ├── filter/
│   │   │       │   │   ├── AuthFilter.java      # 인증 필터
│   │   │       │   │   ├── CORSFilter.java      # CORS 처리 필터
│   │   │       │   │   └── JwtAuthenticationFilter.java # JWT 인증 필터
│   │   │       │   ├── config/
│   │   │       │   │   ├── XmlBeanFactory.java  # XML 기반 Bean 관리
│   │   │       │   │   └── XmlParser.java       # XML 파싱 유틸리티
│   │   │       │   └── util/
│   │   │       │       ├── OpenCrypt.java       # 암호화 유틸리티
│   │   │       │       └── RequestJsonParser.java # JSON 요청 파서
│   │   │       ├── schedule/          # 일정 관리 모듈
│   │   │       │   ├── application/   # 서비스, DTO
│   │   │       │   │   ├── dto/
│   │   │       │   │   │   ├── ScheduleDto.java # 일정 DTO
│   │   │       │   │   │   └── SubScheduleDto.java # 서브 일정 DTO
│   │   │       │   │   └── service/
│   │   │       │   │       └── ScheduleService.java # 일정 관련 서비스
│   │   │       │   ├── domain/        # 엔티티, 레포지토리
│   │   │       │   │   ├── model/
│   │   │       │   │   │   ├── MainScheduleEntity.java # 메인 일정 엔티티
│   │   │       │   │   │   └── SubScheduleEntity.java  # 서브 일정 엔티티
│   │   │       │   │   └── repository/
│   │   │       │   │       └── MainScheduleMapper.java # MyBatis 매퍼
│   │   │       │   └── presentation/
│   │   │       │       └── ScheduleController.java # 일정 관련 API 컨트롤러
│   │   │       ├── user/              # 사용자 관리 모듈
│   │   │       │   ├── application/
│   │   │       │   │   └── service/
│   │   │       │   │       └── UserService.java # 사용자 서비스
│   │   │       │   ├── domain/
│   │   │       │   │   ├── model/
│   │   │       │   │   │   └── UserEntity.java  # 사용자 엔티티
│   │   │       │   │   └── repository/
│   │   │       │   │       └── UserMapper.java  # 사용자 MyBatis 매퍼
│   │   │       │   └── presentation/
│   │   │       │       └── UserController.java  # 사용자 API 컨트롤러
│   │   │       ├── scheduleAi/        # AI 기반 일정 추천 모듈
│   │   │       │   ├── application/
│   │   │       │   │   ├── dto/
│   │   │       │   │   │   └── CreateAiScheduleRequestDTO.java # AI 일정 요청 DTO
│   │   │       │   │   └── service/
│   │   │       │   │       ├── ChatgptService.java   # ChatGPT 서비스
│   │   │       │   │       └── ScheduleAiService.java # AI 일정 추천 서비스
│   │   │       │   └── presentation/
│   │   │       │       └── ScheduleAiController.java # AI 일정 추천 API 컨트롤러
│   │   └── resources/
│   │       ├── mappers/               # MyBatis 매퍼 XML
│   │       │   ├── MainScheduleMapper.xml
│   │       │   └── UserMapper.xml
│   │       └── mybatis-config.xml     # MyBatis 전역 설정
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── beans/                 # Bean 설정
│       │   │   ├── controller.xml
│       │   │   └── model.xml
│       │   └── web.xml                # 서블릿 설정
│       └── META-INF/                  # 메타데이터
└── target/
    └── LuckyWeeky_server.war          # 배포 가능한 WAR 파일
```
