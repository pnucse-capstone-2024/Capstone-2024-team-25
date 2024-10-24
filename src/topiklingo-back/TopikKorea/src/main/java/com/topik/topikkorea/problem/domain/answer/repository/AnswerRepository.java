package com.topik.topikkorea.problem.domain.answer.repository;

import com.topik.topikkorea.problem.domain.answer.Answer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(String id);

    @Query("SELECT a FROM Answer a WHERE a.id IN :ids")
    Optional<List<Answer>> findAllByIds(List<String> ids);
}
