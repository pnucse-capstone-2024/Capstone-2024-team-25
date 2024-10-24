package com.topik.topikkorea.problem.application.question;

import com.topik.topikkorea.problem.application.question.dto.request.QuestionCreateRequest;
import com.topik.topikkorea.problem.application.question.dto.request.QuestionUpdateRequest;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import java.util.List;

public interface QuestionService {
    void createQuestion(final List<QuestionCreateRequest> questionCreateRequests, Problem problem);

    List<Question> getQuestionsByProblem(Problem problem);

    void updateQuestions(final List<QuestionUpdateRequest> requests);
}
