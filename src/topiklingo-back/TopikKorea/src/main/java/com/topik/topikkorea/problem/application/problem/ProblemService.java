package com.topik.topikkorea.problem.application.problem;

import com.topik.topikkorea.problem.application.answer.dto.request.RandomProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemUpdateRequest;
import com.topik.topikkorea.problem.application.problem.dto.response.ProblemResponse;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import java.util.List;

public interface ProblemService {
    void createProblem(final ProblemRequest request);

    List<Problem> createManyProblems(final List<ProblemRequest> problemRequests);

    ProblemResponse getProblemById(String problemId);

    ProblemResponse getProblemByPTypeRandom(RandomProblemRequest request);

    void updateProblems(List<ProblemUpdateRequest> requests);

    List<Question> getQuestions(Problem problem);

    void deleteProblem(String problemId);
}
