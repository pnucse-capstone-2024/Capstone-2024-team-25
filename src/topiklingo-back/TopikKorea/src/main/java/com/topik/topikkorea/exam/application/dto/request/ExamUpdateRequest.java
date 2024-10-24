package com.topik.topikkorea.exam.application.dto.request;

import com.topik.topikkorea.problem.application.problem.dto.request.ProblemUpdateRequest;
import java.util.List;

public record ExamUpdateRequest(
        List<ProblemUpdateRequest> problems
) {
}
