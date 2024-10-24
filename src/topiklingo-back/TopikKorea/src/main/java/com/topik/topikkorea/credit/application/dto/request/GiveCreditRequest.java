package com.topik.topikkorea.credit.application.dto.request;

import com.topik.topikkorea.member.domain.Member;
import lombok.Builder;

@Builder
public record GiveCreditRequest(
        Member creditor,
        Long receiverId,
        int credit
) {
}
