package com.topik.topikkorea.member.application.dto.response;

import com.topik.topikkorea.member.domain.Member;
import lombok.Builder;

@Builder
public record CreditMemberResponse(
        long id,
        String name,
        String email,
        String nation,
        String gender,
        String birth
) {
    public static CreditMemberResponse of(Member member) {
        return CreditMemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nation(member.getNation())
                .gender(member.getGender().name())
                .birth(member.getBirth().toString())
                .build();
    }
}
