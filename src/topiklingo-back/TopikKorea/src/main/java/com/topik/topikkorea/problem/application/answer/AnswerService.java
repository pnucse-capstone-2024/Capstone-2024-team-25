package com.topik.topikkorea.problem.application.answer;

import com.topik.topikkorea.problem.application.answer.dto.request.AnswerCreateRequest;
import com.topik.topikkorea.problem.application.answer.dto.request.AnswerUpdateRequest;
import com.topik.topikkorea.problem.domain.question.Question;
import java.util.List;

public interface AnswerService {
    void createAnswer(final List<AnswerCreateRequest> answerCreateRequests, Question question);

    void updateAnswers(final List<AnswerUpdateRequest> answerCreateRequests);
}
