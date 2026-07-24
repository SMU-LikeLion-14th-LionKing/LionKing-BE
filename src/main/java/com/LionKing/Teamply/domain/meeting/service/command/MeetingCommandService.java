package com.LionKing.Teamply.domain.meeting.service.command;

import com.LionKing.Teamply.domain.meeting.dto.request.MeetingReqDTO;
import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;

public interface MeetingCommandService {
    MeetingResDTO.MeetingCreateRes createMeetingMinute(Long projectId, Long userId, MeetingReqDTO.MeetingCreateReq req);
    MeetingResDTO.MeetingUpdateRes updateMeetingMinute(Long projectId, Long postId, MeetingReqDTO.MeetingUpdateReq req);
    void deleteMeetingMinute(Long projectId, Long postId);
}
