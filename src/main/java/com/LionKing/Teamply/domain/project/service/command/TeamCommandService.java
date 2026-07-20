package com.LionKing.Teamply.domain.project.service.command;

import com.LionKing.Teamply.domain.project.dto.request.CalendarEventCreateRequest;
import com.LionKing.Teamply.domain.project.dto.request.ProjectLinkCreateRequest;
import com.LionKing.Teamply.domain.project.dto.request.ProjectMemberInviteRequest;
import com.LionKing.Teamply.domain.project.dto.request.ProjectMemberUpdateRequest;
import com.LionKing.Teamply.domain.project.dto.response.CalendarEventCreateResponse;
import com.LionKing.Teamply.domain.project.dto.response.ProjectLinkResponse;
import com.LionKing.Teamply.domain.project.dto.response.ProjectMemberInviteResponse;

public interface TeamCommandService {
    ProjectMemberInviteResponse inviteMember(Long projectId, ProjectMemberInviteRequest request);
    void updateMember(Long projectId, Long userId, ProjectMemberUpdateRequest request);
    void removeMember(Long projectId, Long userId);
    ProjectLinkResponse addWorkspace(Long projectId, ProjectLinkCreateRequest request);
    CalendarEventCreateResponse addCalendarEvent(Long projectId, CalendarEventCreateRequest request);
}
