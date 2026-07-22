package com.LionKing.Teamply.domain.post.service.command;

import com.LionKing.Teamply.domain.post.exception.PostErrorCode;
import com.LionKing.Teamply.domain.post.exception.PostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@Profile("!local")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    @Override
    public UploadResult upload(Long projectId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PostException(PostErrorCode.FILE_UPLOAD_FAILED);
        }

        String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
        String uuid = UUID.randomUUID().toString();
        String key = String.format("projects/%d/files/%s-%s", projectId, uuid, originalName);
        String fileType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(fileType)
                    .build();

            s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(
                            file.getInputStream(), file.getSize()));

        } catch (Exception e) {
            log.error("[FileStorage] S3 upload failed", e);
            throw new PostException(PostErrorCode.FILE_UPLOAD_FAILED);
        }

        String fileUrl = buildFileUrl(key);
        log.info("[FileStorage] uploaded: url={}, type={}, size={}", fileUrl, fileType, file.getSize());

        return new UploadResult(fileUrl, fileType);
    }

    @Override
    public void delete(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            log.info("[FileStorage] deleted: key={}", key);
        } catch (Exception e) {
            log.error("[FileStorage] S3 delete failed: key={}", key, e);
            throw new PostException(PostErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public String getDownloadUrl(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        return getPresignedDownloadUrl(key);
    }

    /*-- 다운로드용 presigned URL이 필요할 때 사용 (비공개 버킷이라 직접 URL로는 접근 불가) --*/
    public String getPresignedDownloadUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    /*-- 프론트가 직접 업로드할 때 쓸 presigned PUT URL --*/
    public String getPresignedUploadUrl(String key, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private String buildFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }

    private String extractKeyFromUrl(String fileUrl) {
        URI uri = URI.create(fileUrl);
        String path = uri.getPath();
        return path.startsWith("/") ? path.substring(1) : path;
    }
}