package com.topik.topikkorea.member.application.dto.response;

public record LoginResponse(
        Long memberId,
        String authType,
        String token
) {
}
