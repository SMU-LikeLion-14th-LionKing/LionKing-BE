package com.LionKing.Teamply.domain.project.service.query;

import com.LionKing.Teamply.domain.project.dto.response.CalendarEventResponse;
import com.LionKing.Teamply.domain.project.dto.response.ProjectLinkResponse;
import com.LionKing.Teamply.domain.project.dto.response.ProjectMemberListResponse;
import com.LionKing.Teamply.domain.project.dto.response.ProjectSelectResponse;

import java.util.List;

public interface TeamQueryService {
    ProjectSelectResponse getProject(Long projectId, Long userId);
    List<ProjectMemberListResponse> getMembers(Long projectId);
    List<ProjectLinkResponse> getWorkspaces(Long projectId);
    List<CalendarEventResponse> getCalendarEvents(Long projectId);
}
