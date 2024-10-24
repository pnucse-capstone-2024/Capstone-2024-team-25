package com.topik.topikkorea.exam.application.dto.response;

import com.topik.topikkorea.problem.application.problem.dto.response.ProblemDetailsResponse;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record ExamResponse(
        String examId,
        String title,
        String type,
        Integer year,
        Integer totalQuestions,
        Map<String, List<ProblemDetailsResponse>> config,
        String listenUrl) {
}
