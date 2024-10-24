package com.topik.topikkorea.helper.exam;

import com.topik.topikkorea.exam.application.dto.request.PersonalExamRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.PersonalProblemRequest;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PersonalExamRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private PersonalExamRequestFixture() {
    }

    public static PersonalExamRequest testPersonalExamRequest() {
        int typeCount = randomUtil.generateRandomInt(1, 10);
        List<PersonalProblemRequest> problems = new java.util.ArrayList<>();
        for (int i = 0; i < typeCount; i++) {
            problems.add(PersonalProblemRequestFixture.testPersonalProblemRequest());
        }
        return PersonalExamRequest.builder()
                .title(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .problems(problems)
                .build();
    }
}
