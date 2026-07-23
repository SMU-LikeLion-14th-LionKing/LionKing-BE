package com.LionKing.Teamply.domain.poll.service.command;

import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CastVoteReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CreatePollReq;
import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.UpdateDeadlineReq;

public interface PollCommandService {
    Long createPoll(Long postId, Long userId, CreatePollReq request);
    void castVote(Long pollId, Long userId, CastVoteReq request);
    void closePoll(Long pollId, Long userId);
    void updateDeadline(Long pollId, Long userId, UpdateDeadlineReq request);
}
