package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.dto.request.PostReqDTO;
import com.LionKing.Teamply.domain.post.dto.response.PostResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PostCommandService {
    PostResDTO.PostCreateRes createTask(Long projectId, Long userId, PostReqDTO.TaskCreateReq req);

    PostResDTO.PostCreateRes createQuestion(Long projectId, Long userId, PostReqDTO.QuestionCreateReq req);

    PostResDTO.PostUpdateRes updateTask(Long postId, Long userId, PostReqDTO.PostUpdateReq req);

    PostResDTO.PostUpdateRes updateQuestion(Long postId, Long userId, PostReqDTO.PostUpdateReq req);

    void deletePost(Long postId, Long userId);

    PostResDTO.FileUploadRes uploadFile(Long projectId, Long userId, MultipartFile file);

    void deleteFile(Long fileId, Long userId);

    PostResDTO.NoticeCreateRes createNotice(Long projectId, Long userId, PostReqDTO.NoticeCreateReq req);

    PostResDTO.FileDownloadUrlRes getFileDownloadUrl(Long fileId, Long userId);

}
