package com.topik.topikkorea.problem.application.problem.dto.request;

import lombok.Builder;

@Builder
public record PersonalProblemRequest(
        String problemType,
        String problemCount
) {
}
