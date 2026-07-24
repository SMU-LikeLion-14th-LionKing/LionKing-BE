package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.converter.PostConverter;
import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import com.LionKing.Teamply.domain.post.entity.Attachment;
import com.LionKing.Teamply.domain.post.entity.Post;
import com.LionKing.Teamply.domain.post.entity.PostType;
import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import com.LionKing.Teamply.domain.post.repository.AttachmentRepository;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final AttachmentRepository attachmentRepository;
    private final com.LionKing.Teamply.domain.post.repository.PostReactionRepository postReactionRepository;
    private final com.LionKing.Teamply.domain.comment.repository.CommentRepository commentRepository;
    private final FileStorageService fileStorageService;
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

    /*--작업게시글 작성--*/
    @Override
    public PostResDTO.PostCreateRes createTask(Long projectId, Long userId, PostReqDTO.TaskCreateReq req) {
        Project project = findProject(projectId);
        User user = findUser(userId);

        Post post = PostConverter.toTask(req, project, user);
        Post savedPost = postRepository.save(post);

        if (req.attachments() != null) {
            req.attachments().forEach(a ->
                    attachmentRepository.save(PostConverter.toAttachment(a.fileUrl(), a.fileType(), savedPost))
            );
        }
        return PostConverter.toPostCreateRes(savedPost);
    }

    /*--질문 게시글 작성--*/
    @Override
    public PostResDTO.PostCreateRes createQuestion(Long projectId, Long userId, PostReqDTO.QuestionCreateReq req) {
        Project project = findProject(projectId);
        User user = findUser(userId);

        Post post = PostConverter.toQuestion(req, project, user);
        Post savedPost = postRepository.save(post);

        if (req.attachments() != null) {
            req.attachments().forEach(a ->
                    attachmentRepository.save(PostConverter.toAttachment(a.fileUrl(), a.fileType(), savedPost))
            );
        }
        return PostConverter.toPostCreateRes(savedPost);
    }

    /*--작업게시글 수정--*/
    @Override
    public PostResDTO.PostUpdateRes updateTask(Long postId, Long userId, PostReqDTO.PostUpdateReq req) {
        return updatePostInternal(postId, userId, req, PostType.TASK);
    }

    /*--질문게시글 수정--*/
    @Override
    public PostResDTO.PostUpdateRes updateQuestion(Long postId, Long userId, PostReqDTO.PostUpdateReq req) {
        return updatePostInternal(postId, userId, req, PostType.QUESTION);
    }
    private PostResDTO.PostUpdateRes updatePostInternal(Long postId, Long userId, PostReqDTO.PostUpdateReq req, String expectedType) {
        Post post = findPost(postId);
        validateAuthor(post, userId);
        if (!expectedType.equals(post.getType())) {
            throw new PostException(PostErrorCode.POST_INVALID_TYPE);
        }
        post.updateContent(req.title(), req.content());

        if (req.attachments() != null) {
            attachmentRepository.deleteAllByPost_Id(post.getId());
            req.attachments().forEach(a -> {
                if (a.fileUrl() != null && !a.fileUrl().isBlank()) {
                    attachmentRepository.save(PostConverter.toAttachment(a.fileUrl(), a.fileType(), post));
                }
            });
        }
        return PostConverter.toPostUpdateRes(post);
    }

    /*--게시글 삭제--*/
    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = findPost(postId);
        validateAuthor(post, userId);

        if ("회의록".equals(post.getType()) || "투표".equals(post.getType())) {
            throw new PostException(PostErrorCode.POST_INVALID_TYPE);
        }

        // 1. 연관된 댓글, 반응, 첨부파일 삭제
        commentRepository.deleteAllByPost_Id(post.getId());
        postReactionRepository.deleteAllByPost_Id(post.getId());
        attachmentRepository.deleteAllByPost_Id(post.getId());
        
        // 2. 게시글 삭제
        postRepository.delete(post);
    }

    /*--첨부파일 업로드--*/
    @Override
    public PostResDTO.FileUploadRes uploadFile(Long projectId, Long userId, MultipartFile file) {
        // 프로젝트 존재 여부만 우선 확인 (권한 검증은 프로젝트 도메인에서 추후 추가하면될 것 같아여)
        findProject(projectId);
        findUser(userId);

        FileStorageService.UploadResult result = fileStorageService.upload(projectId, file);

        Attachment attachment = Attachment.builder()
                .fileUrl(result.fileUrl())
                .fileType(result.fileType())
                .build();

        Attachment saved = attachmentRepository.save(attachment);
        return PostConverter.toFileUploadRes(saved);
    }

    /*--첨부파일 삭제--*/
    @Override
    public void deleteFile(Long fileId, Long userId) {
        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new PostException(PostErrorCode.ATTACHMENT_NOT_FOUND));

        // 파일이 특정 게시글에 소속되어 있다면 작성자만 삭제 가능하도록 검증
        if (attachment.getPost() != null && !attachment.getPost().isAuthor(userId)) {
            throw new PostException(PostErrorCode.POST_FORBIDDEN);
        }

        fileStorageService.delete(attachment.getFileUrl());
        attachmentRepository.delete(attachment);
    }

    private Project findProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GeneralErrorCode.NOT_FOUND_404));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    private void validateAuthor(Post post, Long userId) {
        if (!post.isAuthor(userId)) {
            throw new PostException(PostErrorCode.POST_FORBIDDEN);
        }
    }

    @Override
    public PostResDTO.FileDownloadUrlRes getFileDownloadUrl(Long fileId, Long userId) {
        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new PostException(PostErrorCode.ATTACHMENT_NOT_FOUND));

        String downloadUrl = fileStorageService.getDownloadUrl(attachment.getFileUrl());

        return new PostResDTO.FileDownloadUrlRes(downloadUrl);
    }
}
