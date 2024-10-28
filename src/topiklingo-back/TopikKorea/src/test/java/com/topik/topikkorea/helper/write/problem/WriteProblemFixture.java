package com.topik.topikkorea.helper.write.problem;

import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import java.security.NoSuchAlgorithmException;

public class WriteProblemFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private WriteProblemFixture() {
    }

    public static WriteProblem testWriteProblem(WriteProblemType wType) {
        return WriteProblem.builder()
                .problem(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .WType(wType)
                .build();
    }

    public static WriteProblem testIdWriteProblem(WriteProblemType wType) {
        WriteProblem writeProblem = WriteProblem.builder()
                .problem(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .WType(wType)
                .build();

        return writeProblem.testWriteProblemIdSetting(
                Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
