package com.topik.topikkorea.helper.problem.problem;

import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ProblemRequestFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ProblemRequestFixture() {
    }

    public static ProblemRequest testProblemRequest(Problem problem) {
        return ProblemRequest.builder()
                .uuid(problem.getId())
                .PType(problem.getPType().name())
                .problem(problem.getProblem())
                .EType(problem.getEType().name())
                .example(problem.getExample())
                .tags(List.of())
                .questions(List.of())
                .build();
    }
}
