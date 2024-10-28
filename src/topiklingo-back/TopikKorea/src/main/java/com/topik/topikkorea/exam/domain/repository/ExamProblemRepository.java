package com.topik.topikkorea.exam.domain.repository;

import com.topik.topikkorea.exam.domain.ExamProblem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamProblemRepository extends JpaRepository<ExamProblem,Long> {
}
