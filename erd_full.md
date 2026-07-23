<USER_REQUEST>
### 1. users (사용자)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| email | varchar | 이메일 |
| password | varchar | 비밀번호 |
| name | varchar | 이름 |
| position | varchar | 직무/소개용 |
| profile_image_url | varchar | 프로필 사진 경로 |
| bio | text | 자기소개 |
| career | text | 경력 |
| created_at | timestamp | 가입일시 |

### 2. password_reset_tokens (비밀번호 재설정)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| user_id | integer | FK → users.id |
| code | varchar | 이메일로 전송된 인증코드 |
| expires_at | timestamp | 인증코드 만료 시각 |
| is_used | boolean | 사용 여부 (재사용 방지) |
| created_at | timestamp | 생성일시 |

### 3. projects (프로젝트/팀)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| name | varchar | 프로젝트(팀)명 |
| project_type | varchar | 프로젝트 유형 |
| title | varchar | 프로젝트 주제 (대시보드 표시용) |
| deadline | datetime | 마감기한 |
| progress_rate | float | 실제 진행률 |
| ai_progress_rate | float | AI 추정 진행률 (작업진행도 70% + 일정진행도 30%) |
| created_at | timestamp | 생성일시 |

### 4. project_members (프로젝트-사용자 연결)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| user_id | integer | FK → users.id |
| project_id | integer | FK → projects.id |
| role | varchar | 팀장/팀원 (권한용, 프로젝트당 팀장 1명) |
| position | varchar | 팀원이 직접 입력하는 역할 소개 |
| joined_at | timestamp | 합류일시 |

### 5. project_links (팀 워크스페이스 링크)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| project_id | integer | FK → projects.id |
| name | varchar | 워크스페이스 이름 (예: 피그마, 노션) |
| url | varchar | 협업 링크 URL |
| created_at | timestamp | 등록일시 |
<truncated 5634 bytes>
, 중복투표 방지) |
| created_at | timestamp | 투표일시 |

### 19. ai_poll_feedback (AI 협업 도우미 개입 기록)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| poll_id | integer | FK → polls.id |
| situation_type | varchar | 의견대립/판단기준불일치/주제이탈/결론미도출 |
| summary | text | AI가 정리한 상황 요약 |
| suggestion | text | AI의 행동 제안 |
| created_at | timestamp | 개입일시 |

### 20. ai_briefings (AI 주간 브리핑)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| project_id | integer | FK → projects.id |
| summary | text | 주요 소통 이슈 요약 |
| updated_at | timestamp | 마지막 업데이트 시각 |

### 21. ai_priority_items (우선순위 TOP 항목)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| ai_briefing_id | integer | FK → ai_briefings.id |
| rank | integer | 순위 (1, 2, 3) |
| content | varchar | 우선순위 작업 내용 |
| deadline | datetime | 마감일 |

### 22. ai_priority_item_assignees (우선순위 항목 담당자)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| priority_item_id | integer | FK → ai_priority_items.id |
| user_id | integer | FK → users.id (담당자, 여러 명 가능) |

### 23. ai_issues (AI가 발견한 이슈)

| 컬럼명 | 타입 | 설명 |
| --- | --- | --- |
| id | integer | PK |
| project_id | integer | FK → projects.id |
| category | varchar | 이슈 종류 (소통오류/일정지연/회의록미해결/댓글참여부족) |
| risk_level | varchar | 낮음/중간/높음 |
| cause | text | 이슈 원인 |
| suggestion | text | 개선 제안 |
| created_at | timestamp | 발견일시 |

### 24. notifications (알림)

이거 erd 구조거든 이거 바탕으로 회의록 그냥 도메인 새로파서 만드는게 나은가?
</USER_REQUEST>
<ADDITIONAL_METADATA>
The current local time is: 2026-07-20T22:05:03+09:00.
</ADDITIONAL_METADATA>