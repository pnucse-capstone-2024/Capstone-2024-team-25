package com.topik.topikkorea.exam.application.dto.request;

import com.topik.topikkorea.problem.application.problem.dto.request.PersonalProblemRequest;
import java.util.List;
import lombok.Builder;

@Builder
public record PersonalExamRequest(
        String title,
        List<PersonalProblemRequest> problems

) {
}
