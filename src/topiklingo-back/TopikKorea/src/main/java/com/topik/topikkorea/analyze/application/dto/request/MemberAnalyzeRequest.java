package com.topik.topikkorea.analyze.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public record MemberAnalyzeRequest(
        @Schema(description = "유형/전체갯수/맞춘갯수 이렇게 string을 생성한 리스트", example = "[\"LISTEN_1_PROBLEM_TYPE_1/10/9\", \"LISTEN_1_PROBLEM_TYPE_3/5/2\"]")
        String[] MemberAnalyze
) {
}
