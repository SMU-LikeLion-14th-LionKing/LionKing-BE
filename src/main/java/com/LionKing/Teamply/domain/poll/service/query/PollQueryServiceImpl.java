package com.LionKing.Teamply.domain.poll.service.query;

import com.LionKing.Teamply.domain.poll.converter.PollConverter;
import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.*;
import com.LionKing.Teamply.domain.poll.entity.Poll;
import com.LionKing.Teamply.domain.poll.entity.PollResult;
import com.LionKing.Teamply.domain.poll.exception.PollErrorCode;
import com.LionKing.Teamply.domain.poll.exception.PollException;
import com.LionKing.Teamply.domain.poll.repository.PollRepository;
import com.LionKing.Teamply.domain.poll.repository.PollResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollQueryServiceImpl implements PollQueryService {

    private final PollRepository pollRepository;
    private final PollResultRepository pollResultRepository;

    @Override
    public PollDetailRes getPollDetail(Long pollId, Long userId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollException(PollErrorCode.POLL_NOT_FOUND));

        List<PollResult> allResults = pollResultRepository.findAllByPollId(pollId);

        List<Long> votedOptionIds = allResults.stream()
                .filter(res -> res.getUser().getId().equals(userId))
                .map(res -> res.getPollOption().getId())
                .collect(Collectors.toList());

        return PollConverter.toPollDetailRes(poll, votedOptionIds);
    }

    @Override
    public PollResultRes getPollResult(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new PollException(PollErrorCode.POLL_NOT_FOUND));

        List<PollResult> allResults = pollResultRepository.findAllByPollId(pollId);
        
        int totalVoters = (int) allResults.stream().map(res -> res.getUser().getId()).distinct().count();

        Map<Long, List<PollResult>> resultsByOption = allResults.stream()
                .collect(Collectors.groupingBy(res -> res.getPollOption().getId()));

        return PollConverter.toPollResultRes(poll, totalVoters, resultsByOption);
    }
}
