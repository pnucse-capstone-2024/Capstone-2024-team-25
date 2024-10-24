package com.topik.topikkorea.exam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.ExamType;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.helper.exam.ExamFixture;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ExamRepositoryTest {
    @Autowired
    private ExamRepository examRepository;

    private Exam exam;

    @BeforeEach
    public void init() {
        exam = ExamFixture.testIdExam();
    }

    @Test
    @Transactional
    @DisplayName("[success] Exam 저장 성공")
    public void insertExam_Exam_저장_성공() {
        // when
        Exam savedExam = examRepository.save(exam);

        // then
        assertThat(savedExam).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] Exam 조회 성공")
    public void getExam_Exam_조회_성공() {
        // given
        Exam savedExam = examRepository.save(exam);

        // when
        Exam findExam = examRepository.findById(savedExam.getId()).get();

        // then
        assertThat(findExam).isEqualTo(savedExam);
    }

    @Test
    @Transactional
    @DisplayName("[success] Exam 삭제 성공")
    public void deleteExam_Exam_삭제_성공() {
        // given
        Exam savedExam = examRepository.save(exam);

        // when
        examRepository.deleteById(savedExam.getId());

        // then
        assertThat(examRepository.findById(savedExam.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] ExamType Exam 조회 성공")
    public void findByExamName_ExamType_Exam_조회_성공() {
        // given
        examRepository.save(exam);
        Exam exam2 = ExamFixture.testIdTypeExam(exam.getType());
        examRepository.save(exam2);

        // when
        List<Exam> savedExams = examRepository.findAllExams().get();

        assertThat(savedExams.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("[success] ExamType Exam 조회 성공 - 다른 ExamType 포힘")
    public void findByExamType_ExamType_Exam_조회_성공_다른_ExamType_포함() {
        // given
        examRepository.save(exam);
        int examNumber = exam.getType().ordinal();
        examNumber += ThreadLocalRandom.current().nextInt(1, ExamType.values().length - 1);
        examNumber %= ExamType.values().length;

        Exam exam2 = ExamFixture.testIdTypeExam(ExamType.values()[examNumber]);
        examRepository.save(exam2);

        // when
        List<Exam> savedExams = examRepository.findByType(exam.getType()).get();

        // then
        assertThat(savedExams.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    @DisplayName("[success] Exam 전체 조회 성공")
    public void findAllExams_Exam_전체_조회_성공() {
        // given
        examRepository.save(exam);
        Exam exam2 = ExamFixture.testIdExam();
        examRepository.save(exam2);
        Exam exam3 = ExamFixture.testIdExam();
        examRepository.save(exam3);

        // when
        List<Exam> savedExams = examRepository.findAllExams().get();

        // then
        assertThat(savedExams.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @DisplayName("[success] Exam 전체 조회 성공 - 삭제 제외")
    public void findAllExams_Exam_전체_조회_성공_삭제_제외() {
        // given
        examRepository.save(exam);
        Exam exam2 = ExamFixture.testIdExam();
        examRepository.save(exam2);
        Exam exam3 = ExamFixture.testIdExam();
        examRepository.save(exam3);

        // when
        examRepository.deleteById(exam2.getId());
        List<Exam> savedExams = examRepository.findAllExams().get();

        // then
        assertThat(savedExams.size()).isEqualTo(2);
    }
}
