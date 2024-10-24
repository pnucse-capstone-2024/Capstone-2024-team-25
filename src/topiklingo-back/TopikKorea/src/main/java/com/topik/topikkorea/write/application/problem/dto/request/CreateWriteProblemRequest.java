package com.topik.topikkorea.write.application.problem.dto.request;

import com.topik.topikkorea.write.application.question.dto.request.CreateWriteQuestionRequest;
import java.util.List;

public record CreateWriteProblemRequest(
        String problem,
        String WType,
        List<CreateWriteQuestionRequest> questions
) {
}
