package com.topik.topikkorea.write.application.answer.dto.request;

import lombok.Builder;

@Builder
public record CreateWriteAnswerRequest(
        String answer,

        int questionId
) {
}
