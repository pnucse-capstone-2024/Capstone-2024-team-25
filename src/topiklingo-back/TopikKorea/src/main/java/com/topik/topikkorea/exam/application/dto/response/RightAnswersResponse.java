package com.topik.topikkorea.exam.application.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RightAnswersResponse(
        String questionId,
        Integer score,
        Integer rightAnswer
) {}
