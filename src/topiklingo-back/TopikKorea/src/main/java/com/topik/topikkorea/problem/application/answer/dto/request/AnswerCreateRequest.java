package com.topik.topikkorea.problem.application.answer.dto.request;

import java.util.UUID;
import lombok.Builder;

@Builder
public record AnswerCreateRequest(
      String uuid,
      String AType,
      String answer
) {
}
