package com.topik.topikkorea.write.application.problem.dto.response;

import com.topik.topikkorea.write.application.question.dto.response.WriteQuestionResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class WriteProblemResponse {
    private final Long problemId;
    @Setter
    private String problem;
    private final String WType;
    private final List<WriteQuestionResponse> questions;

    @Builder

    public WriteProblemResponse(Long problemId, String problem, String WType,
                                List<WriteQuestionResponse> questions) {
        this.problemId = problemId;
        this.problem = problem;
        this.WType = WType;
        this.questions = questions;
    }
}
