package com.topik.topikkorea.exam.application.dto.request;

import com.topik.topikkorea.write.application.problem.dto.request.CreateWriteProblemRequest;
import java.util.List;

public record CreateWriteExamRequest(
        String uuid,
        String title,
        String type,
        Integer year,
        List<String> tags,
        List<CreateWriteProblemRequest> problems
) {
}
