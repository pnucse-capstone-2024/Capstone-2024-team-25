package com.topik.topikkorea.write.application.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class WriteQuestionResponse {
    private final Long id;
    private final String question;
    @Setter
    private int questionNumber;
    private final String questionExample;
    private final int score;


    @Builder
    public WriteQuestionResponse(Long id, String question, String questionExample, int score) {
        this.id = id;
        this.question = question;
        this.questionExample = questionExample;
        this.score = score;
    }
}

