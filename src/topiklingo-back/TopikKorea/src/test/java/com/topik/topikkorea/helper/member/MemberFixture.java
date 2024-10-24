package com.topik.topikkorea.helper.member;


import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class MemberFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private MemberFixture() {
    }

    public static Member testMember(AuthType authType, String provider) {
        return Member.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .email(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .authType(authType)
                .provider(provider)
                .providerId(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .isDeleted(false)
                .build();
    }

    public static Member testIdMember(Long memberId, AuthType authType, String provider, Gender gender, Center center,
                                      Gather gather) {
        Member member = Member.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .email(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .authType(authType)
                .provider(provider)
                .providerId(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .isDeleted(false)
                .build();

        MemberDetailRequest request = MemberDetailRequest.builder()
                .nation(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .gender(gender.name())
                .birth(randomUtil.generateRandomDate(1900, 2023).toString())
                .department(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .build();

        member.updateMemberDetail(request);

        member.updateMemberCenter(center);
        member.updateMemberGather(gather);

        long testMemberId;

        testMemberId = Objects.requireNonNullElseGet(memberId,
                () -> Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));

        return member.testMemberIdSetting(testMemberId);
    }
}
