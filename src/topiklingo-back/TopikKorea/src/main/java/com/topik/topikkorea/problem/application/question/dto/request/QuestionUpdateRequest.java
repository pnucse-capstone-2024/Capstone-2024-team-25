package com.topik.topikkorea.problem.application.question.dto.request;

import com.topik.topikkorea.problem.application.answer.dto.request.AnswerUpdateRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record QuestionUpdateRequest(
        String questionId,
        String question,
        String example,

        List<AnswerUpdateRequest> answers
) {
}
