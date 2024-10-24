package com.topik.topikkorea.credit.application.dto.request;

import com.topik.topikkorea.member.domain.Member;
import lombok.Builder;

@Builder
public record GetCreditsRequest(
        Member member
) {
}
