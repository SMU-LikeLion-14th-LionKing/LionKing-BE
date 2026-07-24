package com.LionKing.Teamply.domain.meeting.service.command;

import com.LionKing.Teamply.domain.meeting.converter.MeetingConverter;
import com.LionKing.Teamply.domain.meeting.dto.request.MeetingReqDTO;
import com.LionKing.Teamply.domain.meeting.dto.response.MeetingResDTO;
import com.LionKing.Teamply.domain.meeting.entity.MeetingAttendee;
import com.LionKing.Teamply.domain.meeting.entity.MeetingMinute;
import com.LionKing.Teamply.domain.meeting.exception.MeetingErrorCode;
import com.LionKing.Teamply.domain.meeting.exception.MeetingException;
import com.LionKing.Teamply.domain.meeting.repository.ActionItemRepository;
import com.LionKing.Teamply.domain.meeting.repository.MeetingAttendeeRepository;
import com.LionKing.Teamply.domain.meeting.repository.MeetingMinuteRepository;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.global.apiPayload.code.GeneralErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MeetingCommandServiceImpl implements MeetingCommandService {

    private final MeetingMinuteRepository meetingMinuteRepository;
    private final MeetingAttendeeRepository meetingAttendeeRepository;
    private final ActionItemRepository actionItemRepository;
    private final PostRepository postRepository;
    private final com.LionKing.Teamply.domain.comment.repository.CommentRepository commentRepository;
    private final com.LionKing.Teamply.domain.post.repository.PostReactionRepository postReactionRepository;
    private final com.LionKing.Teamply.domain.post.repository.AttachmentRepository attachmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public MeetingResDTO.MeetingCreateRes createMeetingMinute(Long projectId, Long userId, MeetingReqDTO.MeetingCreateReq req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        User author = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        // 1. Post 엔티티 생성 (회의록 타입)
        Post post = Post.builder()
                .project(project)
                .user(author)
                .title(req.meetingTitle())
                .content(req.rawContent())
                .type("회의록")
                .build();
        postRepository.save(post);

        // 2. MeetingMinute 생성
        MeetingMinute meetingMinute = MeetingConverter.toMeetingMinute(req, post);
        meetingMinuteRepository.save(meetingMinute);

        // 3. 참석자 매핑
        saveAttendees(req.attendeeUserIds(), meetingMinute);

        return MeetingConverter.toMeetingCreateRes(meetingMinute);
    }

    @Override
    public MeetingResDTO.MeetingUpdateRes updateMeetingMinute(Long projectId, Long postId, MeetingReqDTO.MeetingUpdateReq req) {
        MeetingMinute meetingMinute = meetingMinuteRepository.findByPostId(postId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND));

        // 소속 프로젝트 검증
        if (!meetingMinute.getPost().getProject().getId().equals(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_FORBIDDEN);
        }

        // 1. 회의록 내용 수정
        meetingMinute.updateMeeting(req.meetingTitle(), req.meetingDate(), req.rawContent());

        // 2. 참석자 목록 갱신 (기존 삭제 후 새로 매핑)
        meetingAttendeeRepository.deleteAllByMeetingMinuteId(meetingMinute.getId());
        saveAttendees(req.attendeeUserIds(), meetingMinute);

        return MeetingConverter.toMeetingUpdateRes(meetingMinute);
    }

    @Override
    public void deleteMeetingMinute(Long projectId, Long postId) {
        MeetingMinute meetingMinute = meetingMinuteRepository.findByPostId(postId)
                .orElseThrow(() -> new MeetingException(MeetingErrorCode.MEETING_NOT_FOUND));

        if (!meetingMinute.getPost().getProject().getId().equals(projectId)) {
            throw new ProjectException(ProjectErrorCode.PROJECT_FORBIDDEN);
        }

        Post post = meetingMinute.getPost();

        // 1. 연관 데이터 삭제
        commentRepository.deleteAllByPost_Id(post.getId());
        postReactionRepository.deleteAllByPost_Id(post.getId());
        attachmentRepository.deleteAllByPost_Id(post.getId());
        actionItemRepository.deleteAllByMeetingMinuteId(meetingMinute.getId());
        meetingAttendeeRepository.deleteAllByMeetingMinuteId(meetingMinute.getId());

        // 2. 본문 및 게시글 삭제
        meetingMinuteRepository.delete(meetingMinute);
        postRepository.delete(post);
    }

    private void saveAttendees(List<Long> attendeeIds, MeetingMinute meetingMinute) {
        if (attendeeIds != null && !attendeeIds.isEmpty()) {
            List<MeetingAttendee> attendees = attendeeIds.stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404)))
                    .map(user -> MeetingAttendee.builder()
                            .meetingMinute(meetingMinute)
                            .user(user)
                            .build())
                    .collect(Collectors.toList());
            meetingAttendeeRepository.saveAll(attendees);
        }
    }
}
