package com.LionKing.Teamply.domain.poll.service.command;

import com.LionKing.Teamply.domain.poll.converter.PollConverter;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CastVoteReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CreatePollReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.UpdateDeadlineReq;
import com.LionKing.Teamply.domain.poll.entity.Poll;
import com.LionKing.Teamply.domain.poll.entity.PollOption;
import com.LionKing.Teamply.domain.poll.entity.PollResult;
import com.LionKing.Teamply.domain.poll.entity.PollStatus;
import com.LionKing.Teamply.domain.poll.exception.PollErrorCode;
import com.LionKing.Teamply.domain.poll.exception.PollException;
import com.LionKing.Teamply.domain.poll.repository.PollOptionRepository;
import com.LionKing.Teamply.domain.poll.repository.PollRepository;
import com.LionKing.Teamply.domain.poll.repository.PollResultRepository;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.exception.UserErrorCode;
import com.LionKing.Teamply.domain.user.exception.UserException;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class PollCommandServiceImpl implements PollCommandService {

    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollResultRepository pollResultRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Long createPoll(Long postId, Long userId, CreatePollReq request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.isAuthor(userId)) {
            throw new PostException(PostErrorCode.POST_FORBIDDEN);
        }

        Poll poll = PollConverter.toPoll(request, post);

        request.options().forEach(optReq -> {
            PollOption option = PollConverter.toPollOption(optReq.content(), optReq.imageUrl());
            poll.addOption(option);
        });

        return pollRepository.save(poll).getId();
    }

    @Override
    public void castVote(Long pollId, Long userId, CastVoteReq request) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollException(PollErrorCode.POLL_NOT_FOUND));

        if (poll.getStatus() == PollStatus.CLOSED || LocalDateTime.now().isAfter(poll.getDeadline())) {
            throw new PollException(PollErrorCode.POLL_CLOSED);
        }

        if (!poll.getIsMultipleChoice() && request.optionIds().size() > 1) {
            throw new PollException(PollErrorCode.MULTIPLE_CHOICE_NOT_ALLOWED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 기존 투표 내역 삭제
        pollResultRepository.deleteByPollIdAndUserId(pollId, userId);

        // 새로운 투표 내역 저장
        for (Long optionId : request.optionIds()) {
            PollOption option = pollOptionRepository.findById(optionId)
                    .orElseThrow(() -> new PollException(PollErrorCode.POLL_OPTION_NOT_FOUND));

            PollResult result = PollConverter.toPollResult(poll, option, user);
            pollResultRepository.save(result);
        }
    }

    @Override
    public void closePoll(Long pollId, Long userId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollException(PollErrorCode.POLL_NOT_FOUND));

        if (!poll.getPost().isAuthor(userId)) {
            throw new PollException(PollErrorCode.POLL_FORBIDDEN);
        }

        poll.closePoll();
    }

    @Override
    public void updateDeadline(Long pollId, Long userId, UpdateDeadlineReq request) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollException(PollErrorCode.POLL_NOT_FOUND));

        if (!poll.getPost().isAuthor(userId)) {
            throw new PollException(PollErrorCode.POLL_FORBIDDEN);
        }

        poll.updateDeadline(request.newDeadline());
    }
}
