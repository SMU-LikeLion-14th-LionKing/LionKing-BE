package com.LionKing.Teamply.domain.ai.service.query;

import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;

public interface AiQueryService {
    ProgressRateRes getAiProgressRate(Long projectId);
    PriorityBriefingRes getWeeklyPriorities(Long projectId);
    PriorityItemRes getPriorityDetail(Long projectId, Long priorityId);
    IssueListRes getAiIssues(Long projectId);
    IssueRes getAiIssueDetail(Long projectId, Long issueId);
}
