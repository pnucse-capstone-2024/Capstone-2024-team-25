package com.topik.topikkorea.exam.application.dto.response;

import com.topik.topikkorea.write.application.problem.dto.response.WriteProblemResponse;
import java.util.List;
import java.util.Map;
import lombok.Builder;

@Builder
public record WriteExamResponse(
        String examId,
        String title,
        String type,
        Integer year,
        Integer totalQuestions,
        Map<String, List<WriteProblemResponse>> config,
        String listenUrl) {
}
