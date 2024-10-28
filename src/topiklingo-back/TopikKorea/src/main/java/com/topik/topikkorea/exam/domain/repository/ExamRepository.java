package com.topik.topikkorea.exam.domain.repository;

import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.ExamType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    @Query("SELECT e FROM Exam e WHERE e.id = :id AND e.isDeleted = false")
    Optional<Exam> findById(String id);

    @Modifying
    @Query("UPDATE Exam e SET e.isDeleted = true WHERE e.id = :id")
    void deleteById(String id);

    @Query("SELECT e FROM Exam e WHERE e.type = :type AND e.isDeleted = false")
    Optional<List<Exam>> findByType(ExamType type);

    @Query("SELECT e FROM Exam e WHERE e.isDeleted = false")
    Optional<List<Exam>> findAllExams();
}
