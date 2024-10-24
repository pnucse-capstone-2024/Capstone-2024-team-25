package com.topik.topikkorea.exam.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.ExamProblem;
import com.topik.topikkorea.exam.domain.repository.ExamProblemRepository;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.helper.exam.ExamFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
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
public class ExamProblemRepositoryTest {
    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ExamProblemRepository examProblemRepository;

    private ExamProblem examProblem;

    private ExamProblem examProblem2;

    private Exam exam;

    private Problem problem;

    private Problem problem2;

    @Transactional
    @BeforeEach
    public void init() {
        exam = ExamFixture.testIdExam();
        exam = examRepository.save(exam);

        problem = ProblemFixture.testIdProblem();
        problem2 = ProblemFixture.testIdProblem();
        problem = problemRepository.save(problem);
        problem2 = problemRepository.save(problem2);

        examProblem = ExamProblem.builder()
                .exam(exam)
                .problem(problem)
                .build();

        examProblem2 = ExamProblem.builder()
                .exam(exam)
                .problem(problem2)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("ExamProblem 전체 저장 테스트")
    public void saveAll_ExamProblem_전체_저장_테스트() {
        // when
        List<ExamProblem> examProblems =
                examProblemRepository.saveAll(List.of(examProblem, examProblem2));

        // then
        assertThat(examProblems).hasSize(2);
        assertThat(examProblems.getFirst().getId()).isNotNull();
    }
}
