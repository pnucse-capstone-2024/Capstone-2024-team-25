package com.topik.topikkorea.helper.gather;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class GatherFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private GatherFixture() {
    }

    public static Gather testGather(Center center, Member teacher) {
        return Gather.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .center(center)
                .teacher(teacher)
                .build();
    }

    public static Gather testIdGather(Center center, Member teacher) {
        Gather gather = Gather.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .center(center)
                .teacher(teacher)
                .build();

        return gather.testGatherIdSetting(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
