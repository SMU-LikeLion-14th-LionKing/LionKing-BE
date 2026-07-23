package com.LionKing.Teamply.domain.user.service.query;

import com.LionKing.Teamply.domain.post.repository.PostRepository;
import com.LionKing.Teamply.domain.user.dto.response.UserResponse;
import com.LionKing.Teamply.domain.user.entity.User;
import com.LionKing.Teamply.domain.user.exception.UserErrorCode;
import com.LionKing.Teamply.domain.user.exception.UserException;
import com.LionKing.Teamply.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

}