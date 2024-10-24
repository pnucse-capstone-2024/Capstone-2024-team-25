package com.topik.topikkorea.credit.application.dto.request;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.member.domain.Member;
import lombok.Builder;

@Builder
public record UseCreditRequest(
        Member member,
        Exam exam
) {
}
