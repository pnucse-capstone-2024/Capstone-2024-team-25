package com.topik.topikkorea.problem.application.question.dto.request;

import com.topik.topikkorea.problem.application.answer.dto.request.AnswerCreateRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record QuestionCreateRequest(
        String uuid,
        String QEType,
        String questionProblem,
        Integer score,

        Integer rightAnswer,
        String explain,
        String example,
        List<AnswerCreateRequest> answers
) {
}
