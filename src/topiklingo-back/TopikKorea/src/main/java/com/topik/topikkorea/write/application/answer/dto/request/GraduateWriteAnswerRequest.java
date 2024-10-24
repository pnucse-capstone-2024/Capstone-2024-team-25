package com.topik.topikkorea.write.application.answer.dto.request;

import lombok.Builder;

@Builder
public record GraduateWriteAnswerRequest(
        int answerId,
        String reason,
        int score
) {
}
