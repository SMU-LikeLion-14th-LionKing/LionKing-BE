package com.LionKing.Teamply.domain.ai.service.command;

import com.LionKing.Teamply.domain.ai.dto.request.AiReqDTO.*;
import com.LionKing.Teamply.domain.ai.dto.response.AiResDTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AiCommandServiceImpl implements AiCommandService {

    private final GeminiService geminiService;
    private final com.LionKing.Teamply.domain.project.repository.ProjectRepository projectRepository;

    @Override
    public CollaborateManagerRes analyzeCollaboration(CollaborateManagerReq request) {
        return geminiService.getCollaborateFeedback(request.content());
    }

    @Override
    public MeetingMinutesRes extractMeetingMinutes(MeetingMinutesReq request) {
        MeetingMinutesRes response = geminiService.getMeetingSummary(request.meetingNotes());
        
        if (request.projectId() != null) {
            com.LionKing.Teamply.domain.project.entity.Project project = projectRepository.findById(request.projectId())
                    .orElseThrow(() -> new com.LionKing.Teamply.domain.project.exception.ProjectException(com.LionKing.Teamply.domain.project.exception.ProjectErrorCode.PROJECT_NOT_FOUND));
            
            // 첫 회의록에서 추출한 전체 작업 개수 저장 (기존 값이 0일 때만 덮어쓰거나 항상 덮어쓰도록 설계)
            project.updateTotalTaskCount(response.totalTaskCount());
        }
        
        return response;
    }

    @Override
    public ScheduleParseRes parseSchedules(ScheduleParseReq request) {
        return geminiService.parseSchedules(request.scheduleNotes());
    }
}
