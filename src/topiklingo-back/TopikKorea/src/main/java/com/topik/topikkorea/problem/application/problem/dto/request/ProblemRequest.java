package com.topik.topikkorea.problem.application.problem.dto.request;

import com.topik.topikkorea.problem.application.question.dto.request.QuestionCreateRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record ProblemRequest(
        String uuid,
        String problem,
        String PType,

        String EType,

        String example,
        List<String> tags,

        List<QuestionCreateRequest> questions
) {
}
