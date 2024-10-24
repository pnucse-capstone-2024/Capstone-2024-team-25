package com.topik.topikkorea.helper.problem.question;

import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.domain.question.QuestionExampleType;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class QuestionFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private QuestionFixture() {
    }

    public static Question testIdQuestion(Problem problem) {
        QuestionExampleType[] exampleTypes = QuestionExampleType.values();

        return Question.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .QEType(exampleTypes[randomUtil.generateRandomInt(0, (exampleTypes.length - 1))].name())
                .questionProblem(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .score(randomUtil.generateRandomInt(0, 100))
                .rightAnswer(randomUtil.generateRandomInt(1, 4))
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .explain(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .problem(problem)
                .build();
    }
}
