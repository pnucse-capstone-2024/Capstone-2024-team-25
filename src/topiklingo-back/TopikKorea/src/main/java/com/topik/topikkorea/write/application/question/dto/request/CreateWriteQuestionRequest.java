package com.topik.topikkorea.write.application.question.dto.request;

public record CreateWriteQuestionRequest(
        String question,
        String example,
        int score
) {
}
