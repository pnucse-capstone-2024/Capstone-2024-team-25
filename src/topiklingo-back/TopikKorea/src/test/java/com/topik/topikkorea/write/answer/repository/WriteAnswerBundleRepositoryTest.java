package com.topik.topikkorea.write.answer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerBundleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WriteAnswerBundleRepositoryTest {
    @Autowired
    private WriteAnswerBundleRepository writeAnswerBundleRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @PersistenceContext
    EntityManager entityManager;

    private WriteAnswerBundle bundle;

    @Transactional
    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        memberRepository.save(member);

        bundle = WriteAnswerBundle.builder()
                .member(member)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] WriteAnswerBundle를 저장에 성공")
    public void save_write_answer_bundle() {
        writeAnswerBundleRepository.save(bundle);

        assertThat(bundle.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] WriteAnswerBundle를 업데이트에 성공")
    public void update_write_answer_bundle() {
        bundle = writeAnswerBundleRepository.save(bundle);

        writeAnswerBundleRepository.updateIsGraduated(bundle.getId(), true);

        entityManager.flush();

        entityManager.clear();

        WriteAnswerBundle updatedBundle = writeAnswerBundleRepository.findById(bundle.getId()).get();

        assertThat(updatedBundle.isGraduated()).isTrue();
    }
}
