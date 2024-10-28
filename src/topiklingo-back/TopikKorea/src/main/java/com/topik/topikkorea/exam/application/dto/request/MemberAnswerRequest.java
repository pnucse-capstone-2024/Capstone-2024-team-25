package com.topik.topikkorea.exam.application.dto.request;

import com.topik.topikkorea.analyze.application.dto.request.MemberAnalyzeRequest;
import lombok.Builder;

@Builder
public record MemberAnswerRequest(
        String memberAnswers,
        String realAnswers,
        int score,
        MemberAnalyzeRequest memberAnalyzeRequest
) {
}
