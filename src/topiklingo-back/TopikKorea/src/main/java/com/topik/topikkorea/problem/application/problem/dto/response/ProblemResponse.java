package com.topik.topikkorea.problem.application.problem.dto.response;

import com.topik.topikkorea.problem.application.question.dto.response.QuestionResponse;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.Builder;

@Builder
public record ProblemResponse(
        String problemId,
        String PType,

        String example,

        String problem,
        List<QuestionResponse> questions) {
//    public static List<ProblemResponse> of(final List<Problem> problems) {
//        problems.sort(Comparator.comparing(p -> ProblemType.valueOf(p.getPType().name()), Comparator.reverseOrder()));
//        problems.forEach(p -> System.out.println(p.getPType().name()));
//        return problems.stream()
//                .map(problem -> new ProblemResponse(
//                            problem.getPType().toString(),
//                            problem.getExample(),
//                            problem.getProblem(),
//                            QuestionResponse.of(problem.getQuestions())
//                    )).toList();
//    }
}
