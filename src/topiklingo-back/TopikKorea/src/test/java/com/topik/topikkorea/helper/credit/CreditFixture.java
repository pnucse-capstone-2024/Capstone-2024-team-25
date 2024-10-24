package com.topik.topikkorea.helper.credit;

import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import java.security.NoSuchAlgorithmException;

public class CreditFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private CreditFixture() {
    }

    public static Credit testCredit(Member creditor, Member receiver, boolean used) {
        return Credit.builder()
                .creditor(creditor)
                .receiver(receiver)
                .used(used)
                .build();
    }
}
