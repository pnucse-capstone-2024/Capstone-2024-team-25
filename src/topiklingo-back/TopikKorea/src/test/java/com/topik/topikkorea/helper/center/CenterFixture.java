package com.topik.topikkorea.helper.center;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class CenterFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private CenterFixture() {
    }

    public static Center testCenter() {
        return Center.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .nation(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .address(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .build();
    }

    public static Center testIdCenter() {
        Center center = Center.builder()
                .name(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .nation(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .address(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .build();

        return center.testCenterIdSetting(Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
