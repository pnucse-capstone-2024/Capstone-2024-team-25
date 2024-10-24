package com.topik.topikkorea.write.domain.answer.repository;

import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WriteAnswerBundleRepository extends JpaRepository<WriteAnswerBundle, Long> {

    @Modifying
    @Query("update WriteAnswerBundle w set w.isGraduated = :isGraduated where w.id = :id")
    void updateIsGraduated(Long id, boolean isGraduated);
}
