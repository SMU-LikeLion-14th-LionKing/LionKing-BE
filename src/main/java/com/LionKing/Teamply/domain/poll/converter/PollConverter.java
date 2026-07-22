package com.LionKing.Teamply.domain.poll.converter;

import com.LionKing.Teamply.domain.poll.dto.request.PollReqDTO.CreatePollReq;
import com.LionKing.Teamply.domain.poll.dto.response.PollResDTO.*;
import com.LionKing.Teamply.domain.poll.entity.Poll;
import com.LionKing.Teamply.domain.poll.entity.PollOption;
import com.LionKing.Teamply.domain.poll.entity.PollResult;
import com.LionKing.Teamply.domain.poll.entity.PollStatus;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.user.entity.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PollConverter {

    public static Poll toPoll(CreatePollReq req, Post post) {
        return Poll.builder()
                .post(post)
                .question(req.question())
                .deadline(req.deadline())
                .status(PollStatus.ONGOING)
                .isMultipleChoice(req.isMultipleChoice())
                .build();
    }

    public static PollOption toPollOption(String content, String imageUrl) {
        return PollOption.builder()
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }

    public static PollResult toPollResult(Poll poll, PollOption option, User user) {
        return PollResult.builder()
                .poll(poll)
                .pollOption(option)
                .user(user)
                .build();
    }

    public static PollDetailRes toPollDetailRes(Poll poll, List<Long> votedOptionIds) {
        List<OptionDetailRes> optionResList = poll.getOptions().stream()
                .map(opt -> new OptionDetailRes(opt.getId(), opt.getContent(), opt.getImageUrl()))
                .collect(Collectors.toList());

        return new PollDetailRes(
                poll.getId(),
                poll.getQuestion(),
                poll.getDeadline(),
                poll.getStatus().name(),
                poll.getIsMultipleChoice(),
                optionResList,
                !votedOptionIds.isEmpty(),
                votedOptionIds
        );
    }

    public static PollResultRes toPollResultRes(Poll poll, int totalVoters, Map<Long, List<PollResult>> resultsByOption) {
        List<OptionResultRes> optionResultResList = poll.getOptions().stream()
                .map(opt -> {
                    List<PollResult> votesForOption = resultsByOption.getOrDefault(opt.getId(), List.of());
                    List<VoterInfo> voters = votesForOption.stream()
                            .map(vote -> new VoterInfo(
                                    vote.getUser().getId(),
                                    vote.getUser().getName(),
                                    vote.getUser().getProfileImageUrl()
                            ))
                            .collect(Collectors.toList());

                    return new OptionResultRes(
                            opt.getId(),
                            opt.getContent(),
                            opt.getImageUrl(),
                            voters.size(),
                            voters
                    );
                })
                .collect(Collectors.toList());

        return new PollResultRes(
                poll.getId(),
                poll.getQuestion(),
                totalVoters,
                optionResultResList
        );
    }
}
