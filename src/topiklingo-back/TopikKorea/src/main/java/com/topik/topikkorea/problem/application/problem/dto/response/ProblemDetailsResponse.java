package com.topik.topikkorea.problem.application.problem.dto.response;

import com.topik.topikkorea.problem.application.question.dto.response.QuestionResponse;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class ProblemDetailsResponse {
    private final String problemId;
    @Setter
    private String problem;
    private final String example;
    private final List<QuestionResponse> questions;

    public ProblemDetailsResponse(String problemId, String problem, String example, List<QuestionResponse> questions) {
        this.problemId = problemId;
        this.problem = problem;
        this.example = example;
        this.questions = questions;
    }
}