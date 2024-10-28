package com.topik.topikkorea.member.utils.jwt;

import com.topik.topikkorea.member.domain.Member;

public interface JwtTokenProvider {
    String generateToken(Member member);

    String generateToken(Long memberId, ExpireDateSupplier expireDateSupplier);

    void validateToken(String token);

    String extractMemberId(String token);
}
