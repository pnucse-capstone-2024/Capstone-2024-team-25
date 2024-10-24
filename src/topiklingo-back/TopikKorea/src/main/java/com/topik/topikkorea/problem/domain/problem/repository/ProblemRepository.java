package com.topik.topikkorea.problem.domain.problem.repository;

import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Problem findById(String id);

    void deleteById(String id);

    Optional<List<Problem>> findByPType(ProblemType problemType);

    @Query("SELECT p FROM Problem p WHERE p.PType = :problemType AND p.id IN ("
            + "SELECT ep.problem.id FROM ExamProblem ep WHERE ep.exam.id IN ("
            + "SELECT e.id FROM Exam e WHERE LOWER(e.title) LIKE '%generated%'"
            + "))")
    List<Problem> findShuffledPType(ProblemType problemType);

    @Query("SELECT p FROM Problem p WHERE p.id IN :ids")
    Optional<List<Problem>> findAllByIds(List<String> ids);
}
