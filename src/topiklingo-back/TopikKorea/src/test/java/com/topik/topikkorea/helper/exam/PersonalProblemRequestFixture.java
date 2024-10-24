package com.topik.topikkorea.helper.exam;

import com.topik.topikkorea.problem.application.problem.dto.request.PersonalProblemRequest;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class PersonalProblemRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private PersonalProblemRequestFixture() {
    }

    public static PersonalProblemRequest testPersonalProblemRequest() {
        return PersonalProblemRequest.builder()
                .problemType(
                        ProblemType.values()[randomUtil.generateRandomInt(0, (ProblemType.values().length - 1))].name())
                .problemCount(randomUtil.generateRandomInt(1, 5) + "")
                .build();
    }
}
