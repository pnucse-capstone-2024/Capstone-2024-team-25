package com.topik.topikkorea.utils;

import java.time.LocalDate;

public interface RandomUtil {
    String generateRandomCode(final char leftLimit, final char rightLimit, final int limit);

    String generateRandomCountCode(final char leftLimit, final char rightLimit, final int leftCountLimit,
                                   final int rightCountLimit);

    LocalDate generateRandomDate(final int startYear, final int endYear);

    int generateRandomInt(int leftLimit, int rightLimit);
}
