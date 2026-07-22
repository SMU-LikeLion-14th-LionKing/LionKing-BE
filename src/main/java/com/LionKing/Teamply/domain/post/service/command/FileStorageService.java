package com.LionKing.Teamply.domain.post.service.command;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /*-- 실제 스토리지(S3 등)에 업로드하고 접근 가능한 URL을 반환 --*/
    //AWS에서 제공하는 클라우드 서비스
    UploadResult upload(Long projectId, MultipartFile file);

    /*-- 저장된 파일 삭제 --*/
    void delete(String fileUrl);

    record UploadResult(String fileUrl, String fileType) {}
}