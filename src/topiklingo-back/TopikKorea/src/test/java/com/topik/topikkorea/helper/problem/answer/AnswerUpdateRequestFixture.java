package com.topik.topikkorea.helper.problem.answer;

import com.topik.topikkorea.problem.application.answer.dto.request.AnswerUpdateRequest;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class AnswerUpdateRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private AnswerUpdateRequestFixture() {
    }

    public static AnswerUpdateRequest testAnswerUpdateRequest(String answerId) {
        return AnswerUpdateRequest.builder()
                .answerId(answerId)
                .answer(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .build();
    }
}
