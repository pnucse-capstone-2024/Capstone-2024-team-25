package com.topik.topikkorea.helper.problem.problem;

import com.topik.topikkorea.problem.domain.problem.ExampleType;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class ProblemFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ProblemFixture() {
    }

    public static Problem testIdProblem() {
        return Problem.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .PType(ProblemType.values()[randomUtil.generateRandomInt(0, (ProblemType.values().length - 1))].name())
                .problem(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .EType(ExampleType.values()[randomUtil.generateRandomInt(0, (ExampleType.values().length - 1))].name())
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .build();
    }

    public static Problem testIdPTypeProblem(ProblemType problemType) {
        return Problem.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .PType(problemType.name())
                .problem(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .EType(ExampleType.values()[randomUtil.generateRandomInt(0, (ExampleType.values().length - 1))].name())
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .build();
    }
}
