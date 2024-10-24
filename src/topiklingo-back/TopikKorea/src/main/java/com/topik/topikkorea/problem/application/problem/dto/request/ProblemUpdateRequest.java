package com.topik.topikkorea.problem.application.problem.dto.request;

import com.topik.topikkorea.problem.application.question.dto.request.QuestionUpdateRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record ProblemUpdateRequest(
        String problemId,

        String example,

        List<QuestionUpdateRequest> questions
) {
}
