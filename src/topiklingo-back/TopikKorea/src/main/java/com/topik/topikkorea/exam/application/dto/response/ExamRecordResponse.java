package com.topik.topikkorea.exam.application.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ExamRecordResponse(
        long id,
        String examId,
        String examName,
        String examType,
        int score,
        String memberAnswers,
        String realAnswers,
        LocalDateTime createdAt
) {
}
