package com.topik.topikkorea.member.application.dto.request;

public record UpdateAuthRequest(
    String email,
    String authType
) {
}
