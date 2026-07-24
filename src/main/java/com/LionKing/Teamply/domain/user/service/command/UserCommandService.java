package com.LionKing.Teamply.domain.user.service.command;

import com.LionKing.Teamply.domain.user.dto.request.ChangePasswordRequest;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {

    void changePassword(Long userId, ChangePasswordRequest request);

    UserResponse.ProfileImageUpdateRes updateProfileImage(Long userId, MultipartFile image);

    void deleteProfileImage(Long userId);
}