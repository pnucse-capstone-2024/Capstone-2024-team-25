package com.topik.topikkorea.problem.application.answer.dto.response;

import com.topik.topikkorea.problem.domain.answer.Answer;
import java.util.List;

public record AnswerResponse(
        String answerId,
        String answer) {

    public static List<AnswerResponse> of(final List<Answer> answers){
        return answers.stream()
                .map(answer -> new AnswerResponse(
                        answer.getId(),
                        answer.getAnswer()
                )).toList();
    }
}
