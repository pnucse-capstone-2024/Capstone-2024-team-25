package com.topik.topikkorea.analyze.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.analyze.domain.MemberAnalyze;
import com.topik.topikkorea.analyze.domain.repository.MemberAnalyzeRepository;
import com.topik.topikkorea.helper.analyze.MemberAnalyzeFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class MemberAnalyzeRepositoryTest {
    @Autowired
    private MemberAnalyzeRepository memberAnalyzeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Member member;

    private MemberAnalyze memberAnalyze;

    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        member = memberRepository.save(member);
        memberAnalyze = MemberAnalyzeFixture.testMemberAnalyze(member);
    }

    @Test
    @Transactional
    @DisplayName("[success] 분석 저장 성공")
    public void insertMember_분석_저장_성공() {
        // given
        memberAnalyzeRepository.save(memberAnalyze);

        // when
        MemberAnalyze savedMemberAnalyze = memberAnalyzeRepository.findById(memberAnalyze.getId()).get();

        // then
        assertThat(savedMemberAnalyze.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 분석 조회 성공 - 빈 리스트")
    public void findMember_분석_조회_성공_빈_리스트() {
        // when
        List<MemberAnalyze> result = memberAnalyzeRepository.findMemberAnalyzesByMemberId(member.getId()).orElseGet(
                List::of);

        // then
        assertThat(result)
                .isEqualTo(List.of());
    }

    @Test
    @Transactional
    @DisplayName("[success] 분석 조회 성공")
    public void findMember_분석_조회_성공() {
        // given
        memberAnalyzeRepository.save(memberAnalyze);

        // when
        MemberAnalyze savedMemberAnalyze = memberAnalyzeRepository.findById(memberAnalyze.getId()).get();

        // then
        assertThat(savedMemberAnalyze.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 분석 수정 성공")
    public void updateMember_분석_수정_성공() {
        // given
        memberAnalyzeRepository.save(memberAnalyze);
        MemberAnalyze savedMemberAnalyze = memberAnalyzeRepository.findById(memberAnalyze.getId()).get();

        // when
        memberAnalyzeRepository.updateByMemberIdAndProblemType(
                savedMemberAnalyze.getMember().getId(),
                savedMemberAnalyze.getProblemType(),
                10,
                10
        );

        entityManager.flush();
        entityManager.clear();

        // then
        MemberAnalyze updatedMemberAnalyze = memberAnalyzeRepository.findById(savedMemberAnalyze.getId()).get();

        assertThat(updatedMemberAnalyze.getTotalCount()).isEqualTo(savedMemberAnalyze.getTotalCount() + 10);
        assertThat(updatedMemberAnalyze.getCorrectCount()).isEqualTo(savedMemberAnalyze.getCorrectCount() + 10);
    }
}
