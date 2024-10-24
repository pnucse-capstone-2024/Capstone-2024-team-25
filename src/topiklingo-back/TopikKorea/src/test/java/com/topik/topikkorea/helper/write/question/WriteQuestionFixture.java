package com.topik.topikkorea.helper.write.question;

import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import java.security.NoSuchAlgorithmException;

public class WriteQuestionFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private WriteQuestionFixture() {
    }

    public static WriteQuestion testWriteQuestion(WriteProblem problem) {
        return WriteQuestion.builder()
                .question(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .score(randomUtil.generateRandomInt(1, 100))
                .problem(problem)
                .build();
    }

    public static WriteQuestion testIdWriteQuestion(WriteProblem problem) {
        WriteQuestion writeQuestion = WriteQuestion.builder()
                .question(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .score(randomUtil.generateRandomInt(1, 100))
                .problem(problem)
                .build();

        return writeQuestion.testWriteQuestionIdSetting(
                Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
