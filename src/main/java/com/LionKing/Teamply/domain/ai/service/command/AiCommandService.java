package com.LionKing.Teamply.domain.ai.service.command;

import com.LionKing.Teamply.domain.ai.dto.request.AiReqDTO.*;
import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;

public interface AiCommandService {
    CollaborateManagerRes analyzeCollaboration(CollaborateManagerReq request);
    MeetingMinutesRes extractMeetingMinutes(MeetingMinutesReq request);
    ScheduleParseRes parseSchedules(ScheduleParseReq request);
}
