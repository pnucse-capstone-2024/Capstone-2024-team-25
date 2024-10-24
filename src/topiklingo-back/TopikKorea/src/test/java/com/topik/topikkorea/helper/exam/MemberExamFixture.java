package com.topik.topikkorea.helper.exam;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.MemberExam;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class MemberExamFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private MemberExamFixture() {
    }

    public static MemberExam testMemberExam(Member member, Exam exam) {
        int problemCount = randomUtil.generateRandomInt(2, 60);
        return MemberExam.builder()
                .member(member)
                .exam(exam)
                .memberAnswers(randomUtil.generateRandomCode('1', '4', problemCount))
                .realAnswers(randomUtil.generateRandomCode('1', '4', problemCount))
                .score(randomUtil.generateRandomInt(0, 200))
                .build();
    }

    public static MemberExam testIdMemberExam(Member member, Exam exam) {
        int problemCount = randomUtil.generateRandomInt(2, 60);
        MemberExam memberExam = MemberExam.builder()
                .member(member)
                .exam(exam)
                .memberAnswers(randomUtil.generateRandomCode('1', '4', problemCount))
                .realAnswers(randomUtil.generateRandomCode('1', '4', problemCount))
                .score(randomUtil.generateRandomInt(0, 200))
                .build();

        return memberExam.testMemberExamIdSetting(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
