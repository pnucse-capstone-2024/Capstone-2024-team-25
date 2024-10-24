package com.topik.topikkorea.helper.problem.answer;

import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.answer.AnswerType;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class AnswerFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private AnswerFixture() {
    }

    public static Answer testAnswer(Question question) {
        AnswerType[] answerTypes = AnswerType.values();

        return Answer.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .AType(answerTypes[randomUtil.generateRandomInt(0, (answerTypes.length - 1))].name())
                .answer(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .question(question)
                .build();
    }
}
