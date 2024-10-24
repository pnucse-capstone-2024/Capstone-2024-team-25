package com.topik.topikkorea.member.application.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResponse(
        long id,
        String name,
        String email,
        String nation,
        String gender,
        String birth,
        String provider,
        String center,
        String gather
) {
}
