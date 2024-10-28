package com.topik.topikkorea.exam.domain.repository;

import com.topik.topikkorea.exam.application.dto.response.DepartmentMemberRecordResponse;
import com.topik.topikkorea.exam.domain.MemberExam;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberExamRepository extends JpaRepository<MemberExam, Long> {
    @Query("SELECT me FROM MemberExam me WHERE me.exam.id = :examId AND me.member.isDeleted = false AND me.exam.isDeleted = false")
    List<MemberExam> findByExam(String examId);

    @Query("SELECT me FROM MemberExam me WHERE me.member.id = :memberId AND me.member.isDeleted = false AND me.exam.isDeleted = false")
    Optional<List<MemberExam>> findByMemberId(Long memberId);

    @Query("SELECT new com.topik.topikkorea.exam.application.dto.response.DepartmentMemberRecordResponse( " +
            "m.department, m.name, m.email, e.title, me.score, me.createdAt) " +
            "FROM MemberExam me " +
            "JOIN me.member m " +
            "JOIN me.exam e " +
            "WHERE m.department = :department " +
            "AND me.createdAt >= :startDate " +
            "AND me.createdAt < :endDate " +
            "ORDER BY m.name")
    List<DepartmentMemberRecordResponse> findMemberExamRecordsByDepartmentAndDateRange(
            @Param("department") String department,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT new com.topik.topikkorea.exam.application.dto.response.DepartmentMemberRecordResponse( " +
            "m.department, m.name, m.email, e.title, me.score, me.createdAt) " +
            "FROM MemberExam me " +
            "JOIN me.member m " +
            "JOIN me.exam e " +
            "WHERE me.createdAt >= :startDate " +
            "AND me.createdAt < :endDate " +
            "ORDER BY m.name")
    List<DepartmentMemberRecordResponse> findMemberExamRecordsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
