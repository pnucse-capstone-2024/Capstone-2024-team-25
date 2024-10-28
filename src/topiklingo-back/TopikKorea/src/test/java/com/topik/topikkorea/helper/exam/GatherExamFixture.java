package com.topik.topikkorea.helper.exam;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class GatherExamFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private GatherExamFixture() {
    }

    public static GatherExam testGatherExam(Gather gather, Exam exam) {
        return GatherExam.builder()
                .gather(gather)
                .exam(exam)
                .memberCount(randomUtil.generateRandomInt(1, 100000))
                .sum(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)))
                .build();
    }

    public static GatherExam testIdGatherExam(Gather gather, Exam exam) {
        GatherExam gatherExam = GatherExam.builder()
                .gather(gather)
                .exam(exam)
                .memberCount(randomUtil.generateRandomInt(1, 100000))
                .sum(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)))
                .build();

        return gatherExam.testGatherExamIdSetting(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
