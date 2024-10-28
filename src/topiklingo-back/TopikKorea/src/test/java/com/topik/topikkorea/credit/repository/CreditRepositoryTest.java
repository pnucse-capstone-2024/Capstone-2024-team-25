package com.topik.topikkorea.credit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.credit.domain.repository.CreditRepository;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.helper.credit.CreditFixture;
import com.topik.topikkorea.helper.exam.ExamFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CreditRepositoryTest {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CreditRepository creditRepository;

    private Exam exam;

    private Member creditor;

    private Member receiver;

    private Credit credit;

    @Transactional
    @BeforeEach
    public void init() {
        exam = ExamFixture.testIdExam();
        exam = examRepository.save(exam);

        creditor = MemberFixture.testMember(AuthType.ADMIN, LoginProvider.GOOGLE.name());
        creditor = memberRepository.save(creditor);

        receiver = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        receiver = memberRepository.save(receiver);
    }

    @Transactional
    @Test
    @DisplayName("[success] Credit 저장 테스트")
    public void Credit_저장_테스트() {
        credit = CreditFixture.testCredit(creditor, receiver, false);
        credit = creditRepository.save(credit);

        assertThat(credit.getId()).isNotNull();
    }

    @Transactional
    @Test
    @DisplayName("[success] 사용하지 않는 크레딧 조회")
    public void 사용하지_않는_크레딧_조회() {
        credit = CreditFixture.testCredit(creditor, receiver, false);
        Credit credit2 = CreditFixture.testCredit(creditor, receiver, true);

        credit = creditRepository.save(credit);
        credit2 = creditRepository.save(credit2);

        List<Credit> credits = creditRepository.findUnusedCredits(receiver.getId());

        assertThat(credits.size()).isEqualTo(1);
        assertThat(credits.getFirst()).isEqualTo(credit);
    }

    @Transactional
    @Test
    @DisplayName("[success] 사용한 크레딧 조회")
    public void 사용한_크레딧_조회() {
        credit = CreditFixture.testCredit(creditor, receiver, false);
        Credit credit2 = CreditFixture.testCredit(creditor, receiver, true);

        credit = creditRepository.save(credit);
        credit2 = creditRepository.save(credit2);

        List<Credit> credits = creditRepository.findUsedCredits(receiver.getId());

        assertThat(credits.size()).isEqualTo(1);
        assertThat(credits.getFirst()).isEqualTo(credit2);
    }

    @Transactional
    @Test
    @DisplayName("[success] 사용하지 않은 가장 오래된 크레딧 조회")
    public void 사용하지_않은_가장_오래된_크레딧_조회() {
        credit = CreditFixture.testCredit(creditor, receiver, false);
        Credit credit2 = CreditFixture.testCredit(creditor, receiver, false);

        credit = creditRepository.save(credit);
        credit2 = creditRepository.save(credit2);

        Optional<Credit> findedCredit = creditRepository.findOldestCredit(receiver.getId());

        assertThat(findedCredit.get()).isEqualTo(credit);
    }

    @Test
    @Transactional
    @DisplayName("[success] saveAll 다수의 것 저장 성공")
    public void saveAll_다수의_것_저장_성공() {
        // given
        List<Credit> credits = IntStream.range(0, 10)
                .mapToObj(i -> Credit.builder()
                        .creditor(creditor)
                        .receiver(receiver)
                        .used(false)
                        .build())
                .toList();

        // when & then
        creditRepository.saveAll(credits);
    }

    @Test
    @Transactional
    @DisplayName("[success] saveAll 자기 자신 저장 성공")
    public void saveAll_자기_자신_저장_성공() {
        // given
        List<Credit> credits = IntStream.range(0, 10)
                .mapToObj(i -> Credit.builder()
                        .creditor(creditor)
                        .receiver(creditor)
                        .used(false)
                        .build())
                .toList();

        // when & then
        creditRepository.saveAll(credits);
    }
}
