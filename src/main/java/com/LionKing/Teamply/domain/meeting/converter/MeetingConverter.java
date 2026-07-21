package com.LionKing.Teamply.domain.meeting.converter;

import com.LionKing.Teamply.domain.meeting.dto.request.MeetingReqDTO;
import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;
import com.LionKing.Teamply.domain.meeting.entity.ActionItem;
import com.LionKing.Teamply.domain.meeting.entity.MeetingAttendee;
import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import com.LionKing.Teamply.domain.post.entity.Post;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingConverter {

    public static MeetingMinute toMeetingMinute(MeetingReqDTO.MeetingCreateReq req, Post post) {
        return MeetingMinute.builder()
                .post(post)
                .meetingTitle(req.meetingTitle())
                .meetingDate(req.meetingDate())
                .rawContent(req.rawContent())
                .build();
    }

    public static MeetingResDTO.MeetingCreateRes toMeetingCreateRes(MeetingMinute meetingMinute) {
        return new MeetingResDTO.MeetingCreateRes(
                meetingMinute.getId(),
                meetingMinute.getCreatedAt()
        );
    }

    public static MeetingResDTO.MeetingUpdateRes toMeetingUpdateRes(MeetingMinute meetingMinute) {
        return new MeetingResDTO.MeetingUpdateRes(
                meetingMinute.getId(),
                meetingMinute.getUpdatedAt()
        );
    }

    public static MeetingResDTO.MeetingGetRes toMeetingGetRes(
            MeetingMinute meetingMinute, 
            List<MeetingAttendee> attendees, 
            List<ActionItem> actionItems
    ) {
        List<MeetingResDTO.AttendeeRes> attendeeResList = attendees.stream()
                .map(attendee -> new MeetingResDTO.AttendeeRes(
                        attendee.getUser().getId(),
                        attendee.getUser().getName(),
                        attendee.getUser().getEmail()
                ))
                .collect(Collectors.toList());

        List<MeetingResDTO.ActionItemRes> actionItemResList = actionItems.stream()
                .map(item -> new MeetingResDTO.ActionItemRes(
                        item.getId(),
                        item.getContent(),
                        item.getAssignee() != null ? item.getAssignee().getId() : null,
                        item.getDeadline(),
                        item.getStatus(),
                        item.getLinkedPost() != null ? item.getLinkedPost().getId() : null
                ))
                .collect(Collectors.toList());

        return new MeetingResDTO.MeetingGetRes(
                meetingMinute.getId(),
                meetingMinute.getMeetingTitle(),
                meetingMinute.getMeetingDate(),
                meetingMinute.getRawContent(),
                meetingMinute.getDiscussion(),
                meetingMinute.getUndecidedItems(),
                attendeeResList,
                actionItemResList
        );
    }
}
