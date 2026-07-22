package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Slf4j
@Service
@Profile("local")
public class LocalFileStorageService implements FileStorageService {
    private static final String DUMMY_BASE_URL = "https://s3.ap-northeast-2.amazonaws.com/teamply";

    @Override
    public UploadResult upload(Long projectId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PostException(PostErrorCode.FILE_UPLOAD_FAILED);
        }
        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
        String uuid = UUID.randomUUID().toString();
        String url = String.format("%s/projects/%d/files/%s-%s", DUMMY_BASE_URL, projectId, uuid, originalName);
        String fileType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        log.info("[FileStorage] (dummy) uploaded: url={}, type={}, size={}", url, fileType, file.getSize());
        return new UploadResult(url, fileType);
    }

    @Override
    public void delete(String fileUrl) {
        log.info("[FileStorage] (dummy) deleted: url={}", fileUrl);
        // 실제 삭제는 S3FileStorageService에서 처리 (이 클래스는 로컬 개발용 더미)

    }
    @Override
    public String getDownloadUrl(String fileUrl) {
        log.info("[FileStorage] (dummy) download url requested: url={}", fileUrl);
        return fileUrl; // 더미는 그냥 원본 URL 반환
    }
}
