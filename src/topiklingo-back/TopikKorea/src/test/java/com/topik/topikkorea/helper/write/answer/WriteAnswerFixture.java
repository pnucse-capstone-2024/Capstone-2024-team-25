package com.topik.topikkorea.helper.write.answer;

import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.utils.RandomUtil;
import com.topik.topikkorea.utils.RandomUtilImpl;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import java.security.NoSuchAlgorithmException;

public class WriteAnswerFixture {
    private static final RandomUtil randomUtil;

    static {
        try {
            randomUtil = new RandomUtilImpl();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private WriteAnswerFixture() {
    }

    public static WriteAnswer testWriteAnswer(WriteQuestion question, WriteAnswerBundle bundle, Member member) {
        return WriteAnswer.builder()
                .answer(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .question(question)
                .member(member)
                .bundle(bundle)
                .build();
    }

    public static WriteAnswer testIdWriteAnswer(WriteQuestion question, WriteAnswerBundle bundle, Member member) {
        WriteAnswer writeAnswer = WriteAnswer.builder()
                .answer(randomUtil.generateRandomCountCode('A', 'z', 1, 255))
                .question(question)
                .member(member)
                .bundle(bundle)
                .build();

        return writeAnswer.testWriteAnswerIdSetting(
                Long.parseLong(randomUtil.generateRandomCountCode('0', '9', 1, 12)));
    }
}
