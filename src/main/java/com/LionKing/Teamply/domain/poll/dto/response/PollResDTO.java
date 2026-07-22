package com.LionKing.Teamply.domain.poll.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record PollResDTO() {

    public record PollDetailRes(
            Long pollId,
            String question,
            LocalDateTime deadline,
            String status,
            Boolean isMultipleChoice,
            List<OptionDetailRes> options,
            Boolean hasVoted,
            List<Long> votedOptionIds
    ) {}

    public record OptionDetailRes(
            Long optionId,
            String content,
            String imageUrl
    ) {}

    public record PollResultRes(
            Long pollId,
            String question,
            Integer totalVotes,
            List<OptionResultRes> results
    ) {}

    public record OptionResultRes(
            Long optionId,
            String content,
            String imageUrl,
            Integer voteCount,
            List<VoterInfo> voters
    ) {}

    public record VoterInfo(
            Long userId,
            String name,
            String profileImageUrl
    ) {}
}
