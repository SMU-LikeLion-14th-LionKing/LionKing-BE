package com.LionKing.Teamply.domain.poll.service.query;

import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.PollDetailRes;
import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.PollResultRes;

public interface PollQueryService {
    PollDetailRes getPollDetail(Long pollId, Long userId);
    PollResultRes getPollResult(Long pollId);
}
