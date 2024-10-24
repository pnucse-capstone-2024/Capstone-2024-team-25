package com.topik.topikkorea.member.application.dto.request;

import lombok.Builder;

@Builder
public record MemberDetailRequest(
        String nation,
        String gender,
        String birth,
        String department
) {
}
