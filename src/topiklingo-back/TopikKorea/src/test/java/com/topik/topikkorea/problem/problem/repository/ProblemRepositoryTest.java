package com.topik.topikkorea.problem.problem.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
public class ProblemRepositoryTest {
    @Autowired
    private ProblemRepository problemRepository;

    private Problem problem;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
    }

    @Test
    @Transactional
    @DisplayName("[success]문제 저장 성공")
    public void insertProblem_문제_저장() {
        // when
        Problem savedProblem = problemRepository.save(problem);

        // then
        assertThat(savedProblem.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success]문제 삭제 성공")
    public void deleteProblem_문제_삭제() {
        // given
        problemRepository.save(problem);

        // when
        problemRepository.delete(problem);
        Problem deletedProblem = problemRepository.findById(problem.getId());

        // then
        assertThat(deletedProblem).isNull();
    }

    @Test
    @Transactional
    @DisplayName("[success]문제 타입으로 문제 조회 성공")
    public void findByPType_문제_타입으로_문제_조회() {
        // given
        problemRepository.save(problem);

        // when
        Problem savedProblem = problemRepository.findByPType(problem.getPType()).get().getFirst();

        // then
        assertThat(savedProblem.getPType()).isEqualTo(problem.getPType());
    }

    @Test
    @Transactional
    @DisplayName("[success]문제 아이디들로 문제 리스트 조회 성공")
    public void findAllByIds_문제_아이디들로_문제_리스트_조회() {
        // given
        problemRepository.save(problem);
        Problem problem2 = ProblemFixture.testIdProblem();
        problemRepository.save(problem2);

        // when
        List<Problem> savedProblems = problemRepository.findAllByIds(List.of(problem.getId(), problem2.getId())).get();

        // then
        assertThat(savedProblems.size()).isEqualTo(2);
    }
}
