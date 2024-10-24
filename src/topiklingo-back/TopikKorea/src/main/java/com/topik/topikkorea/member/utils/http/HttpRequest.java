package com.topik.topikkorea.member.utils.http;

import com.topik.topikkorea.member.domain.Member;

public interface HttpRequest {
    Member getCurrentMember();

    String validateFromToken();
}
