package com.topik.topikkorea.helper.analyze;

import com.topik.topikkorea.analyze.domain.MemberAnalyze;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class MemberAnalyzeFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private MemberAnalyzeFixture() {
    }

    public static MemberAnalyze testMemberAnalyze(Member member) {
        return MemberAnalyze.builder()
                .member(member)
                .problemType(ProblemType.values()[randomUtil.generateRandomInt(0, ProblemType.values().length - 1)])
                .totalCount(randomUtil.generateRandomInt(50, 100))
                .correctCount(randomUtil.generateRandomInt(0, 50))
                .build();
    }

    public static MemberAnalyze testIdMemberAnalyze(Member member) {
        MemberAnalyze memberAnalyze = MemberAnalyze.builder()
                .member(member)
                .problemType(ProblemType.values()[randomUtil.generateRandomInt(0, ProblemType.values().length - 1)])
                .totalCount(randomUtil.generateRandomInt(50, 100))
                .correctCount(randomUtil.generateRandomInt(0, 50))
                .build();

        return memberAnalyze.testMemberAnalyzeIdSetting(
                Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
