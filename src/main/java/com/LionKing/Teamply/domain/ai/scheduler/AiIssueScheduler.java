package com.LionKing.Teamply.domain.ai.scheduler;

import com.LionKing.Teamply.domain.ai.entity.AiIssue;
import com.LionKing.Teamply.domain.ai.repository.AiIssueRepository;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiIssueScheduler {

    private final ProjectRepository projectRepository;
    private final AiIssueRepository aiIssueRepository;

    /**
     * 6시간마다 실행되는 스케줄러 (매 0, 6, 12, 18시 정각)
     * cron = "0 0 0/6 * * *"
     */
    @Scheduled(cron = "0 0 0/6 * * *")
    @Transactional
    public void generateAiIssuesEvery6Hours() {
        log.info("[AI Scheduler] 6시간 주기 AI 이슈 분석 스케줄러 시작");

        List<Project> allProjects = projectRepository.findAll();

        for (Project project : allProjects) {
            // TODO: 실제로는 해당 프로젝트의 지난 6시간 동안의 채팅, 회의록, 댓글 등을 모아서 Gemini에 분석을 요청해야 함.
            // 지금은 API 틀을 잡는 과정이므로, Mock 데이터를 생성해서 삽입합니다.

            AiIssue mockIssue = AiIssue.builder()
                    .project(project)
                    .category("일정지연")
                    .riskLevel("중간")
                    .cause("백엔드 API 명세서 작성이 예정보다 1일 지연되고 있습니다.")
                    .suggestion("팀 회의를 통해 일정 조정 및 역할 분담 재검토가 필요합니다.")
                    .build();

            aiIssueRepository.save(mockIssue);
            log.info("Project ID: {} 에 대한 AI 발견 이슈 자동 생성 완료", project.getId());
        }

        log.info("[AI Scheduler] 6시간 주기 AI 이슈 분석 스케줄러 종료 (총 {}개 프로젝트 처리)", allProjects.size());
    }
}
