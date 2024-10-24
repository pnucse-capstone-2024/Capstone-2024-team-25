package com.topik.topikkorea.exam.application.dto.request;

import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record ExamRequest(
        String uuid,
        String title,
        String type,
        Integer year,
        List<String> tags,
        List<ProblemRequest> problems) {
}
