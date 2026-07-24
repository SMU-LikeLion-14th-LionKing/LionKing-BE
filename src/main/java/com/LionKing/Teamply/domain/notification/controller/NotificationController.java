package com.LionKing.Teamply.domain.notification.controller;

import com.LionKing.Teamply.domain.notification.dto.response.NotificationPageRes;
import com.LionKing.Teamply.domain.notification.dto.response.NotificationUnreadCountRes;
import com.LionKing.Teamply.domain.notification.service.command.NotificationCommandService;
import com.LionKing.Teamply.domain.notification.service.query.NotificationQueryService;
import com.LionKing.Teamply.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOTIFICATION404_1: 해당 알림을 찾을 수 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "NOTIFICATION403_1: 해당 알림에 접근할 권한이 없습니다.", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = com.LionKing.Teamply.global.common.ApiResponse.class)))
})
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "알림 API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @Operation(summary = "알림 목록 조회 (페이징)", description = "본인의 모든 알림을 최신순으로 페이징 조회합니다.")
    @GetMapping
    public ApiResponse<NotificationPageRes> getNotifications(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return ApiResponse.success("알림 목록 조회 성공", notificationQueryService.getNotifications(userId, page, size));
    }

    @Operation(summary = "알림 목록 요약 조회", description = "최신 알림 4개를 반환합니다.")
    @GetMapping("/summary")
    public ApiResponse<NotificationPageRes> getNotificationSummary(
            @AuthenticationPrincipal Long userId) {
        return ApiResponse.success("알림 목록 요약 조회 성공", notificationQueryService.getNotificationSummary(userId));
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "안 읽은 알림의 총 개수를 반환합니다.")
    @GetMapping("/unread-count")
    public ApiResponse<NotificationUnreadCountRes> getUnreadCount(
            @AuthenticationPrincipal Long userId) {
        return ApiResponse.success("읽지 않은 알림 개수 조회 성공", notificationQueryService.getUnreadCount(userId));
    }

    @Operation(summary = "특정 알림 읽음 처리", description = "특정 알림을 읽음 처리합니다.")
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<Void> readNotification(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long notificationId) {
        notificationCommandService.readNotification(userId, notificationId);
        return ApiResponse.success("알림 읽음 처리 성공");
    }

    @Operation(summary = "모든 알림 읽음 처리", description = "본인의 미확인 알림 전체를 읽음 처리합니다.")
    @PatchMapping("/read-all")
    public ApiResponse<Void> readAllNotifications(
            @AuthenticationPrincipal Long userId) {
        notificationCommandService.readAllNotifications(userId);
        return ApiResponse.success("모든 알림 읽음 처리 성공");
    }
}
