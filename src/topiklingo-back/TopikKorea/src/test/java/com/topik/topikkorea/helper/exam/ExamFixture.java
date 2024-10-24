package com.topik.topikkorea.helper.exam;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.ExamType;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class ExamFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private ExamFixture() {
    }

    public static Exam testIdExam() {
        return Exam.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .title(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .type(ExamType.values()[randomUtil.generateRandomInt(0, (ExamType.values().length - 1))].name())
                .year(randomUtil.generateRandomInt(2000, 2100))
                .build();
    }

    public static Exam testIdTypeExam(ExamType examType) {
        return Exam.builder()
                .uuid(randomUtil.generateRandomCountCode('A', 'z', 1, 20))
                .title(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .type(examType.name())
                .year(randomUtil.generateRandomInt(2000, 2100))
                .build();
    }
}
