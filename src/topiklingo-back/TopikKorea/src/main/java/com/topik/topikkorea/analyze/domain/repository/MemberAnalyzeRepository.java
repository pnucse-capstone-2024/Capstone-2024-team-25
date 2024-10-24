package com.topik.topikkorea.analyze.domain.repository;

import com.topik.topikkorea.analyze.domain.MemberAnalyze;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberAnalyzeRepository extends JpaRepository<MemberAnalyze, Long> {
    @Query("SELECT ma FROM MemberAnalyze ma WHERE ma.member.id = :memberId AND ma.member.isDeleted = false")
    Optional<List<MemberAnalyze>> findMemberAnalyzesByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE MemberAnalyze ma SET ma.totalCount = ma.totalCount + :tryCount, ma.correctCount = ma.correctCount + :correctCount WHERE ma.member.id = :memberId AND ma.problemType = :problemType AND ma.member.isDeleted = false")
    void updateByMemberIdAndProblemType(Long memberId, ProblemType problemType, int tryCount, int correctCount);
}
