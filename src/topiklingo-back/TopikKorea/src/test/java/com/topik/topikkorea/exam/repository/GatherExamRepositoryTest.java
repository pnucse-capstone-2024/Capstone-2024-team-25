package com.topik.topikkorea.exam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.exam.domain.repository.GatherExamRepository;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.exam.ExamFixture;
import com.topik.topikkorea.helper.exam.GatherExamFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class GatherExamRepositoryTest {
    @Autowired
    private GatherExamRepository gatherExamRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private GatherRepository gatherRepository;

    private Gather gather;

    private Center center;

    private Member teacher;

    private Member student;

    private Exam exam;

    @Transactional
    @BeforeEach
    public void init() {
        center = CenterFixture.testCenter();
        center = centerRepository.save(center);

        teacher = MemberFixture.testMember(AuthType.TEACHER, LoginProvider.GOOGLE.name());
        student = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        teacher = memberRepository.save(teacher);
        student = memberRepository.save(student);

        gather = GatherFixture.testGather(center, teacher);
        gather = gatherRepository.save(gather);

        exam = ExamFixture.testIdExam();
        exam = examRepository.save(exam);
    }

    @Test
    @Transactional
    @DisplayName("[success] GatherExam 저장 성공")
    public void saveGatherExam() {
        // given
        GatherExam gatherExam = GatherExamFixture
                .testGatherExam(gather, exam);

        // when
        GatherExam savedGatherExam = gatherExamRepository.save(gatherExam);

        // then
        assertThat(savedGatherExam.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] GatherExam 조회 성공")
    public void findByExamAndGather_GatherExam_조회_성공() {
        // given
        GatherExam gatherExam = GatherExamFixture
                .testGatherExam(gather, exam);
        GatherExam savedGatherExam =
                gatherExamRepository.save(gatherExam);

        // when
        GatherExam foundGatherExam = gatherExamRepository
                .findByExamAndGather(exam, gather).get();

        // then
        assertThat(foundGatherExam).isEqualTo(savedGatherExam);
    }

    @Test
    @Transactional
    @DisplayName("[success] Center의 GatherExam 조회 성공")
    public void findByExamAndCenter_GatherExam_조회_성공() {
        // given
        GatherExam gatherExam = GatherExamFixture
                .testGatherExam(gather, exam);
        GatherExam savedGatherExam =
                gatherExamRepository.save(gatherExam);

        Gather gather2 = GatherFixture.testGather(center, teacher);
        gather2 = gatherRepository.save(gather2);
        GatherExam gatherExam2 = GatherExamFixture
                .testGatherExam(gather2, exam);
        GatherExam savedGatherExam2 =
                gatherExamRepository.save(gatherExam2);

        // when
        List<GatherExam> foundGatherExams = gatherExamRepository
                .findByExamAndCenter(exam, center).get();

        // then
        assertThat(foundGatherExams.size()).isEqualTo(2);
        assertThat(foundGatherExams).contains(savedGatherExam, savedGatherExam2);
    }
}
