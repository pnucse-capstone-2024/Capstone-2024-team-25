package com.topik.topikkorea.problem.domain.question.repository;

import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<List<Question>> getQuestionsByProblem(Problem problem);

    Optional<Question> findById(String id);

    @Query("SELECT q FROM Question q WHERE q.id IN :ids")
    Optional<List<Question>> findAllByIds(List<String> ids);
}
