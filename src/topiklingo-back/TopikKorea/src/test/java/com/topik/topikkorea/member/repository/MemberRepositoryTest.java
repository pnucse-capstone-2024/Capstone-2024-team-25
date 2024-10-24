package com.topik.topikkorea.member.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private GatherRepository gatherRepository;

    @Autowired
    private EntityManager entityManager;

    private Member member;

    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 저장 성공")
    public void 회원_저장_성공() {
        // when
        final Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 조회 성공")
    public void 회원_조회_성공() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final Member foundMember = memberRepository.findById(savedMember.getId()).get();

        // then
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] student 회원 조회 성공")
    public void student_회원_조회_성공() {
        // given
        memberRepository.save(member);
        Member member2 = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        memberRepository.save(member2);
        List<Member> members = List.of(member, member2);

        // when
        final List<Member> foundMembers = memberRepository.findAllStudents();

        // then
        assertThat(foundMembers.size()).isEqualTo(2);
        assertThat(foundMembers).isEqualTo(members);
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 삭제 성공")
    public void 회원_삭제_성공() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        memberRepository.deleteById(savedMember.getId());

        // then
        assertThat(memberRepository.findById(savedMember.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 수정 성공")
    public void 회원_수정_성공() {
        // given
        final Member savedMember = memberRepository.save(member);
        final LocalDate testLocalDate = LocalDate.now();
        final MemberDetailRequest request = MemberDetailRequest.builder()
                .nation("KOR")
                .gender(Gender.MALE.name())
                .birth(testLocalDate.toString())
                .department("testuniv")
                .build();

        // when
        savedMember.updateMemberDetail(request);
        memberRepository.save(savedMember);

        // then
        final Member updatedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(updatedMember.getNation()).isEqualTo("KOR");
        assertThat(updatedMember.getGender()).isEqualTo(Gender.MALE);
        assertThat(updatedMember.getDepartment()).isEqualTo("testuniv");
        assertThat(updatedMember.getBirth()).isEqualTo(testLocalDate);
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 수정 소속 없음 성공")
    public void 회원_수정_소속_없음_성공() {
        // given
        final Member savedMember = memberRepository.save(member);
        final LocalDate testLocalDate = LocalDate.now();
        final MemberDetailRequest request = MemberDetailRequest.builder()
                .nation("KOR")
                .gender(Gender.MALE.name())
                .birth(testLocalDate.toString())
                .department("none")
                .build();

        // when
        savedMember.updateMemberDetail(request);
        memberRepository.save(savedMember);

        // then
        final Member updatedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(updatedMember.getNation()).isEqualTo("KOR");
        assertThat(updatedMember.getGender()).isEqualTo(Gender.MALE);
        assertThat(updatedMember.getDepartment()).isEqualTo("none");
        assertThat(updatedMember.getBirth()).isEqualTo(testLocalDate);
    }

    @Test
    @Transactional
    @DisplayName("[success] provider providerId로 회원 조회 성공")
    public void provider_providerId로_회원_조회_성공() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final Member foundMember = memberRepository.findMemberByProviderAndProviderId(member.getProvider(),
                member.getProviderId()).get();

        // then
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] 회원 권한 수정 성공")
    public void 회원_권한_수정_성공() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        memberRepository.updateAuthTypeByEmail(savedMember.getEmail(), AuthType.TEACHER);
        entityManager.flush();
        entityManager.clear();

        // then
        final Member updatedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(updatedMember.getAuthType()).isEqualTo(AuthType.TEACHER);
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 회원 조회 성공")
    public void 그룹_회원_조회_성공() {
        // given
        Center center = CenterFixture.testCenter();
        centerRepository.save(center);

        Member teacher = MemberFixture.testMember(AuthType.TEACHER, LoginProvider.GOOGLE.name());
        teacher.updateMemberCenter(center);
        memberRepository.save(teacher);

        Gather gather = GatherFixture.testGather(center, teacher);
        gatherRepository.save(gather);

        member.updateMemberCenter(center);
        member.updateMemberGather(gather);
        memberRepository.save(member);

        final Member member2 = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        member2.updateMemberCenter(center);
        member2.updateMemberGather(gather);
        memberRepository.save(member2);

        // when
        final List<Member> foundMembers = memberRepository.findByGatherId(gather.getId()).get();

        // then
        assertThat(foundMembers.size()).isEqualTo(2);
    }
}
