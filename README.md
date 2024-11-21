# 댕댕어디가

## 강아지와 함께하는 반려동물 친화적인 장소 및 활동 공유 플랫폼

이 프로젝트는 강아지와 함께 방문할 수 있는 다양한 장소들을 소개하고, 사용자 맞춤형 추천 시스템과 편리한 관리 기능을 제공하는 플랫폼입니다.
추천 시스템은 강아지의 크기, 사용자 선호도 등을 기반으로 개인화된 추천을 제공합니다. 또한, 사용자는 강아지 프로필을 관리하며, 장소를 즐겨찾기하거나 리뷰를 남길 수 있습니다.  

---
## 주요 기능

### 백엔드 기능
- **리뷰 관리**
  - 리뷰 등록, 조회, 삭제
  - 사용자가 작성한 모든 리뷰 조회
- **추천 시스템**
  - 사용자 맞춤 추천 장소 제공
  - AWS 분석 처리 및 적용
- **위치 관리**
  - 장소 검색 (필터링 포함)
  - 장소 상세 조회
  - AI 리뷰 요약 기능 (GPT 활용)
- **선호 장소 관리**
  - 선호 장소 등록, 수정 및 조회
- **반려동물 관리**
  - 반려동물 정보 등록, 수정, 삭제
  - 즐겨찾기 장소 확인
- **방문 이력 관리**
  - 방문 이력 등록 및 조회
- **알림 시스템**
  - 즐겨찾기된 장소 이벤트 및 행사 알림


### 프론트엔드 기능
- **페이지 퍼블리싱**
  - 검색, 필터링, 상세 페이지
  - 방문 이력 목록, 등록 페이지
- **API 연동**
  - 검색 페이지 및 즐겨찾기 페이지 연동
  - 소셜 로그인 연동 (카카오/구글)
  - 구글 지도
- **회원 관리**
  - 회원가입, 로그인, 로그아웃
  - 회원 정보 등록 및 수정
  - 회원 탈퇴

---

## 기능

- 장소검색기능


- 리뷰 등록 삭제 조회 기능


- 반려동물 등록 조회 수정 삭제 기능


- 즐겨찾기 등록 조회 수정 삭제 기능


---

## 기술 스택

### 백엔드
- **Java Spring Boot**: 강력한 웹 애플리케이션 개발을 위한 프레임워크.
- **Spring Data JPA**: 데이터베이스와의 상호작용을 위한 ORM(Object-Relational Mapping) 라이브러리.
- **Spring Security**: 애플리케이션의 인증 및 권한 관리.
- **MySQL**: 관계형 데이터베이스 관리 시스템(RDBMS).
- **Redis**: 고속 데이터 캐싱을 위한 인메모리 데이터 구조 저장소.

### 인프라 및 클라우드
- **AWS**: 클라우드 호스팅 및 배포 서비스 (EC2, S3, RDS 등).
  
### 테스트
- **JUnit5**: Java 애플리케이션의 단위 테스트를 위한 프레임워크.

### 협업 도구
- **GitHub**: 버전 관리 및 협업을 위한 Git 저장소 호스팅 서비스.
- **Slack**: 팀 간 실시간 커뮤니케이션.
- **Jira**: 프로젝트 관리 및 이슈 추적 시스템.

### 추가 기술
- **CI/CD (GitHub Actions, Jenkins)**: 지속적 통합 및 배포 자동화.
- **Nginx**: 웹 서버 및 리버스 프록시.


## ERD
![댕댕어디가](https://github.com/user-attachments/assets/7e555644-e7e4-4af0-b0d4-cbcffc8ffd54)

