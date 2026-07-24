package com.LionKing.Teamply.domain.ai.scheduler;

import com.LionKing.Teamply.domain.ai.entity.AiBriefing;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItem;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItemAssignee;
import com.LionKing.Teamply.domain.ai.repository.AiBriefingRepository;
import com.LionKing.Teamply.domain.ai.repository.AiPriorityItemAssigneeRepository;
import com.LionKing.Teamply.domain.ai.repository.AiPriorityItemRepository;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.entity.ProjectMember;
import com.LionKing.Teamply.domain.project.repository.ProjectMemberRepository;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiBriefingScheduler {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final AiBriefingRepository aiBriefingRepository;
    private final AiPriorityItemRepository aiPriorityItemRepository;
    private final AiPriorityItemAssigneeRepository aiPriorityItemAssigneeRepository;

    /**
     * 6시간마다 실행되는 스케줄러 (매 0, 6, 12, 18시 정각)
     * cron = "0 0 0/6 * * *"
     */
    @Scheduled(cron = "0 0 0/6 * * *")
    @Transactional
    public void generateAiBriefingsEvery6Hours() {
        log.info("[AI Scheduler] 6시간 주기 주간 브리핑 및 우선순위 분석 스케줄러 시작");

        List<Project> allProjects = projectRepository.findAll();
        Random random = new Random();

        for (Project project : allProjects) {
            // 1. 브리핑(AiBriefing) Mock 데이터 생성
            AiBriefing mockBriefing = AiBriefing.builder()
                    .project(project)
                    .summary("이번 주 팀 프로젝트는 원활히 진행되고 있으나, API 명세서 작성이 지연되고 있습니다. 팀 회의를 통해 일정 조정이 필요해 보입니다.")
                    .build();
            aiBriefingRepository.save(mockBriefing);

            // 해당 프로젝트의 팀원 목록 가져오기
            List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
            if (members.isEmpty()) {
                log.warn("Project ID: {} 에 멤버가 없어 담당자를 배정하지 않고 건너뜁니다.", project.getId());
                continue;
            }

            // 2. 우선순위(AiPriorityItem) 3개 생성
            String[] contents = {
                    "API 명세서 최종 검토 및 확정",
                    "로그인/회원가입 프론트엔드 뷰 구현",
                    "데이터베이스 스키마 설계 및 리뷰"
            };

            for (int i = 0; i < 3; i++) {
                AiPriorityItem mockPriorityItem = AiPriorityItem.builder()
                        .aiBriefing(mockBriefing)
                        .rank(i + 1)
                        .content(contents[i])
                        .deadline(LocalDateTime.now().plusDays(2 + i)) // 마감일은 2~4일 후로 설정
                        .build();
                aiPriorityItemRepository.save(mockPriorityItem);

                // 3. 담당자(AiPriorityItemAssignee) 1명 무작위 배정
                ProjectMember randomMember = members.get(random.nextInt(members.size()));

                AiPriorityItemAssignee mockAssignee = AiPriorityItemAssignee.builder()
                        .priorityItem(mockPriorityItem)
                        .user(randomMember.getUser())
                        .build();
                aiPriorityItemAssigneeRepository.save(mockAssignee);
            }

            log.info("Project ID: {} 에 대한 AI 주간 브리핑 및 우선순위 생성 완료", project.getId());
        }

        log.info("[AI Scheduler] 6시간 주기 주간 브리핑 스케줄러 종료 (총 {}개 프로젝트 처리)", allProjects.size());
    }
}
