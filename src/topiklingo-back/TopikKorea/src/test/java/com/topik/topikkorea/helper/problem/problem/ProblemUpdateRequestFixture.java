package com.topik.topikkorea.helper.problem.problem;

import com.topik.topikkorea.problem.application.problem.dto.request.ProblemUpdateRequest;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ProblemUpdateRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ProblemUpdateRequestFixture() {
    }

    public static ProblemUpdateRequest testProblemUpdateRequest(String problemId) {
        return ProblemUpdateRequest.builder()
                .problemId(problemId)
                .example(randomUtil.generateRandomCountCode('A', 'z', 1, 1000))
                .questions(List.of())
                .build();
    }
}
