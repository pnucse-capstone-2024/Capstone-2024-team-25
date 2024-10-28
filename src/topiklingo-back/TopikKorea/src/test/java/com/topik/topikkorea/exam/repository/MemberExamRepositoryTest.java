package com.topik.topikkorea.exam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.MemberExam;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.exam.domain.repository.MemberExamRepository;
import com.topik.topikkorea.helper.exam.ExamFixture;
import com.topik.topikkorea.helper.exam.MemberExamFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class MemberExamRepositoryTest {
    @Autowired
    private MemberExamRepository memberExamRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberExam memberExam;

    private Member member;

    private Exam exam;

    @Transactional
    @BeforeEach
    public void init() {
        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        member = memberRepository.save(member);

        exam = ExamFixture.testIdExam();
        exam = examRepository.save(exam);

        memberExam = MemberExamFixture.testMemberExam(member, exam);
    }

    @Test
    @Transactional
    @DisplayName("[success] MemberExam 저장 테스트")
    public void saveMemberExam() {
        MemberExam savedMemberExam = memberExamRepository.save(memberExam);

        assertThat(savedMemberExam.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] MemberExam department 조회 테스트")
    public void findByDepartment() {
        memberExamRepository.save(memberExam);
        MemberDetailRequest request = MemberDetailRequest.builder()
                .nation("kr")
                .gender(Gender.MALE.name())
                .birth("1990-01-01")
                .department("inje")
                .build();

        member.updateMemberDetail(request);

        assertThat(memberExamRepository.findMemberExamRecordsByDepartmentAndDateRange(
                "inje",
                LocalDateTime.parse("2000-01-01T00:00:00"),
                LocalDateTime.parse("2024-10-25T00:00:00")
        )).isNotEmpty();
    }
}
