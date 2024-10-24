package com.topik.topikkorea.utils;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomUtilImpl implements RandomUtil {
    private final ThreadLocalRandom threadLocalRandom;

    public RandomUtilImpl() throws NoSuchAlgorithmException {
        this.threadLocalRandom = ThreadLocalRandom.current();
    }

    @Override
    public String generateRandomCode(final char leftLimit, final char rightLimit, final int limit) {
        StringBuilder randomString = new StringBuilder(limit);

        for (int i = 0; i < limit; i++) {
            int randomCodePoint = leftLimit + threadLocalRandom.nextInt(rightLimit - leftLimit + 1);
            randomString.append((char) randomCodePoint);
        }

        return randomString.toString();
    }

    @Override
    public String generateRandomCountCode(final char leftLimit, final char rightLimit, final int leftCountLimit,
                                          final int rightCountLimit) {
        int limit = threadLocalRandom.nextInt(leftCountLimit, rightCountLimit);
        return generateRandomCode(leftLimit, rightLimit, limit);
    }

    @Override
    public LocalDate generateRandomDate(int startYear, int endYear) {
        int day = threadLocalRandom.nextInt(1, 28);
        int month = threadLocalRandom.nextInt(1, 12);
        int year = threadLocalRandom.nextInt(startYear, endYear);

        return LocalDate.of(year, month, day);
    }

    @Override
    public int generateRandomInt(int leftLimit, int rightLimit) {
        return threadLocalRandom.nextInt(leftLimit, rightLimit);
    }
}
