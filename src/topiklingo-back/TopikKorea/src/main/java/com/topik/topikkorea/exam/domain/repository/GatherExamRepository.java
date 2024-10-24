package com.topik.topikkorea.exam.domain.repository;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.gather.domain.Gather;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GatherExamRepository extends JpaRepository<GatherExam, Long> {
    @Query("SELECT ge FROM GatherExam ge WHERE ge.gather.center = :center AND ge.exam = :exam AND ge.gather.center.isDeleted = false AND ge.exam.isDeleted = false")
    Optional<List<GatherExam>> findByExamAndCenter(Exam exam, Center center);

    @Query("SELECT ge FROM GatherExam ge WHERE ge.exam = :exam AND ge.gather = :gather AND ge.exam.isDeleted = false AND ge.gather.center.isDeleted = false")
    Optional<GatherExam> findByExamAndGather(Exam exam, Gather gather);
}
