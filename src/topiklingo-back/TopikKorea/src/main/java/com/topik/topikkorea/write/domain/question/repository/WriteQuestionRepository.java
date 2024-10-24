package com.topik.topikkorea.write.domain.question.repository;

import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteQuestionRepository extends JpaRepository<WriteQuestion, Long> {
    List<WriteQuestion> findAllByProblem(WriteProblem problem);
}
