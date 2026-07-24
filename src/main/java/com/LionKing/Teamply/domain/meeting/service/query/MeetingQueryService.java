package com.LionKing.Teamply.domain.meeting.service.query;

import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;

public interface MeetingQueryService {
    MeetingResDTO.MeetingGetRes getMeetingMinute(Long projectId, Long postId);
}
