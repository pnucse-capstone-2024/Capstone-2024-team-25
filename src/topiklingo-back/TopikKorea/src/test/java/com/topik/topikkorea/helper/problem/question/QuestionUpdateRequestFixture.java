package com.topik.topikkorea.helper.problem.question;

import com.topik.topikkorea.problem.application.question.dto.request.QuestionUpdateRequest;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class QuestionUpdateRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private QuestionUpdateRequestFixture() {
    }

    public static QuestionUpdateRequest testQuestionUpdateRequest(String questionId) {
        return QuestionUpdateRequest.builder()
                .questionId(questionId)
                .question(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .answers(List.of())
                .build();
    }
}
