package com.LionKing.Teamply.domain.ai.converter;

import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;
import com.LionKing.Teamply.domain.ai.entity.AiBriefing;
import com.LionKing.Teamply.domain.ai.entity.AiIssue;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItem;
import com.LionKing.Teamply.domain.ai.entity.AiPriorityItemAssignee;
import com.LionKing.Teamply.domain.project.entity.Project;

import java.util.List;
import java.util.stream.Collectors;

public class AiConverter {

    public static ProgressRateRes toProgressRateRes(Project project) {
        return new ProgressRateRes(
                project.getProgressRate(),
                project.getAiProgressRate()
        );
    }

    public static PriorityAssigneeRes toPriorityAssigneeRes(AiPriorityItemAssignee assignee) {
        return new PriorityAssigneeRes(
                assignee.getUser().getId(),
                assignee.getUser().getName(),
                assignee.getUser().getProfileImageUrl()
        );
    }

    public static PriorityItemRes toPriorityItemRes(AiPriorityItem item, List<AiPriorityItemAssignee> assignees) {
        List<PriorityAssigneeRes> assigneeResList = assignees.stream()
                .map(AiConverter::toPriorityAssigneeRes)
                .collect(Collectors.toList());

        return new PriorityItemRes(
                item.getId(),
                item.getRank(),
                item.getContent(),
                item.getDeadline(),
                assigneeResList
        );
    }

    public static PriorityBriefingRes toPriorityBriefingRes(AiBriefing briefing, List<PriorityItemRes> priorities) {
        return new PriorityBriefingRes(
                briefing.getId(),
                briefing.getSummary(),
                priorities
        );
    }

    public static IssueRes toIssueRes(AiIssue issue) {
        return new IssueRes(
                issue.getId(),
                issue.getCategory(),
                issue.getRiskLevel(),
                issue.getCause(),
                issue.getSuggestion(),
                issue.getCreatedAt()
        );
    }

    public static IssueListRes toIssueListRes(List<AiIssue> issues) {
        List<IssueRes> issueResList = issues.stream()
                .map(AiConverter::toIssueRes)
                .collect(Collectors.toList());
        return new IssueListRes(issueResList);
    }
}
