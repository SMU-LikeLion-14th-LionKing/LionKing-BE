package com.LionKing.Teamply.domain.user.service.command;

import com.LionKing.Teamply.domain.post.service.command.FileStorageService;
import com.LionKing.Teamply.domain.user.dto.request.ChangePasswordRequest;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.exception.UserErrorCode;
import com.LionKing.Teamply.domain.user.exception.UserException;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    /*-- 비밀번호 변경 --*/
    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = findUser(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    /*-- 프로필 사진 변경 --*/
    @Override
    public UserResponse.ProfileImageUpdateRes updateProfileImage(Long userId, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new UserException(UserErrorCode.USER_NOT_FOUND); // 필요 시 별도 에러코드로 교체
        }

        User user = findUser(userId);

        // 기존 프로필 이미지가 있으면 스토리지에서 삭제
        String oldImageUrl = user.getProfileImageUrl();
        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            try {
                fileStorageService.delete(oldImageUrl);
            } catch (Exception e) {
                log.warn("[UserCommand] 기존 프로필 이미지 삭제 실패: url={}, msg={}", oldImageUrl, e.getMessage());
            }
        }

        // 새 이미지 업로드 (userId 를 폴더 키로 사용)
        FileStorageService.UploadResult result = fileStorageService.upload(userId, image);

        // 엔티티의 profileImageUrl 만 업데이트
        user.updateProfile(
                user.getName(),
                user.getPosition(),
                result.fileUrl(),
                user.getBio(),
                user.getCareer()
        );

        return new UserResponse.ProfileImageUpdateRes(user.getId(), result.fileUrl());
    }

    /*-- 프로필 사진 삭제 (기본 이미지로 복원) --*/
    @Override
    public void deleteProfileImage(Long userId) {
        User user = findUser(userId);

        String currentImageUrl = user.getProfileImageUrl();
        if (currentImageUrl != null && !currentImageUrl.isBlank()) {
            try {
                fileStorageService.delete(currentImageUrl);
            } catch (Exception e) {
                log.warn("[UserCommand] 프로필 이미지 삭제 실패: url={}, msg={}", currentImageUrl, e.getMessage());
            }
        }

        user.clearProfileImage();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
