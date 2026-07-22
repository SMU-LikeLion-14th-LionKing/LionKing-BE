package com.LionKing.Teamply.domain.ai.service.query;

import com.LionKing.Teamply.domain.ai.converter.AiConverter;
import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;
import com.LionKing.Teamply.domain.ai.entity.AiBriefing;
import com.LionKing.Teamply.domain.ai.entity.AiIssue;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItem;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItemAssignee;
import com.LionKing.Teamply.domain.ai.exception.AiErrorCode;
import com.LionKing.Teamply.domain.ai.exception.AiException;
import com.LionKing.Teamply.domain.ai.repository.AiBriefingRepository;
import com.LionKing.Teamply.domain.ai.repository.AiIssueRepository;
import com.LionKing.Teamply.domain.ai.repository.AiPriorityItemAssigneeRepository;
import com.LionKing.Teamply.domain.ai.repository.AiPriorityItemRepository;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiQueryServiceImpl implements AiQueryService {

    private final ProjectRepository projectRepository;
    private final AiBriefingRepository aiBriefingRepository;
    private final AiPriorityItemRepository aiPriorityItemRepository;
    private final AiPriorityItemAssigneeRepository aiPriorityItemAssigneeRepository;
    private final AiIssueRepository aiIssueRepository;

    @Override
    public ProgressRateRes getAiProgressRate(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));
        return AiConverter.toProgressRateRes(project);
    }

    @Override
    public PriorityBriefingRes getWeeklyPriorities(Long projectId) {
        // 프로젝트 확인
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        // 가장 최근의 AiBriefing 조회 (없으면 빈 리스트 반환)
        AiBriefing briefing = aiBriefingRepository.findTopByProjectIdOrderByUpdatedAtDesc(projectId)
                .orElse(null);

        if (briefing == null) {
            return new PriorityBriefingRes(null, "등록된 주간 브리핑이 없습니다.", new ArrayList<>());
        }

        List<AiPriorityItem> items = aiPriorityItemRepository.findAllByAiBriefingIdOrderByRankAsc(briefing.getId());
        
        List<PriorityItemRes> priorityResList = items.stream().map(item -> {
            List<AiPriorityItemAssignee> assignees = aiPriorityItemAssigneeRepository.findAllByPriorityItemId(item.getId());
            return AiConverter.toPriorityItemRes(item, assignees);
        }).collect(Collectors.toList());

        return AiConverter.toPriorityBriefingRes(briefing, priorityResList);
    }

    @Override
    public PriorityItemRes getPriorityDetail(Long projectId, Long priorityId) {
        // 프로젝트 접근 로직이나 검증은 필요에 따라 추가
        AiPriorityItem item = aiPriorityItemRepository.findById(priorityId)
                .orElseThrow(() -> new AiException(AiErrorCode.PRIORITY_ITEM_NOT_FOUND));

        List<AiPriorityItemAssignee> assignees = aiPriorityItemAssigneeRepository.findAllByPriorityItemId(priorityId);
        return AiConverter.toPriorityItemRes(item, assignees);
    }

    @Override
    public IssueListRes getAiIssues(Long projectId) {
        // 프로젝트 확인
        projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        List<AiIssue> issues = aiIssueRepository.findAllByProjectIdOrderByCreatedAtDesc(projectId);
        return AiConverter.toIssueListRes(issues);
    }

    @Override
    public IssueRes getAiIssueDetail(Long projectId, Long issueId) {
        AiIssue issue = aiIssueRepository.findById(issueId)
                .orElseThrow(() -> new AiException(AiErrorCode.ISSUE_NOT_FOUND));

        // 해당 프로젝트의 이슈인지 검증
        if (!issue.getProject().getId().equals(projectId)) {
            throw new AiException(AiErrorCode.ISSUE_NOT_FOUND);
        }

        return AiConverter.toIssueRes(issue);
    }
}
