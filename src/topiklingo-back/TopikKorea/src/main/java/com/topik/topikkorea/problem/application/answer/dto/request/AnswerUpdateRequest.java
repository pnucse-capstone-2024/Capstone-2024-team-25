package com.topik.topikkorea.problem.application.answer.dto.request;

import lombok.Builder;

@Builder
public record AnswerUpdateRequest(
        String answerId,

        String answer
) {
}
