package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.converter.PostConverter;
import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.project.entity.Project;
import com.LionKing.Teamply.domain.project.repository.ProjectRepository;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import com.LionKing.Teamply.domain.project.exception.ProjectErrorCode;
import com.LionKing.Teamply.domain.project.exception.ProjectException;
import com.LionKing.Teamply.global.apiPayload.code.GeneralErrorCode;
import com.LionKing.Teamply.global.apiPayload.exception.handler.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public PostResDTO.NoticeCreateRes createNotice(Long projectId, Long userId, PostReqDTO.NoticeCreateReq req) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));

        Post post = PostConverter.toNotice(req, project, user);
        Post savedPost = postRepository.save(post);

        return PostConverter.toNoticeCreateRes(savedPost);
    }
}
