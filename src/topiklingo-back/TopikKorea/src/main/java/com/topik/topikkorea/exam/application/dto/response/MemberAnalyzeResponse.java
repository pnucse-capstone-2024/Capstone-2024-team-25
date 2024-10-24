package com.topik.topikkorea.exam.application.dto.response;

import lombok.Builder;

@Builder
public record MemberAnalyzeResponse(
        long id,
        int correctCount,
        int totalCount,
        String problemType
) {
}
