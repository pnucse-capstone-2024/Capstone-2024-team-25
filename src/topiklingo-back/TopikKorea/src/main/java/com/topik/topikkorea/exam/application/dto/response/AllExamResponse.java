package com.topik.topikkorea.exam.application.dto.response;

import lombok.Builder;

@Builder
public record AllExamResponse(
        String id,
        String title,
        int year
) {
}
