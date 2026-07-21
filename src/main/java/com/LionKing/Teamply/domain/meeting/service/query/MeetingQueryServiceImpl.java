package com.LionKing.Teamply.domain.meeting.service.query;

import com.LionKing.Teamply.domain.meeting.converter.MeetingConverter;
import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;
import com.LionKing.Teamply.domain.meeting.entity.ActionItem;
import com.LionKing.Teamply.domain.meeting.entity.MeetingAttendee;
import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import com.LionKing.Teamply.domain.meeting.exception.MeetingErrorCode;
import com.LionKing.Teamply.domain.meeting.exception.MeetingException;
import com.LionKing.Teamply.domain.meeting.repository.ActionItemRepository;
import com.LionKing.Teamply.domain.meeting.repository.MeetingAttendeeRepository;
import com.LionKing.Teamply.domain.meeting.repository.MeetingMinuteRepository;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeetingQueryServiceImpl implements MeetingQueryService {

    private final MeetingMinuteRepository meetingMinuteRepository;
    private final MeetingAttendeeRepository meetingAttendeeRepository;
    private final ActionItemRepository actionItemRepository;

    @Override
    public MeetingResDTO.MeetingGetRes getMeetingMinute(Long projectId, Long meetingMinuteId) {
        MeetingMinute meetingMinute = meetingMinuteRepository.findById(meetingMinuteId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND));

        if (!meetingMinute.getPost().getProject().getId().equals(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_FORBIDDEN);
        }

        List<MeetingAttendee> attendees = meetingAttendeeRepository.findAllByMeetingMinuteId(meetingMinuteId);
        List<ActionItem> actionItems = actionItemRepository.findAllByMeetingMinuteId(meetingMinuteId);

        return MeetingConverter.toMeetingGetRes(meetingMinute, attendees, actionItems);
    }
}
