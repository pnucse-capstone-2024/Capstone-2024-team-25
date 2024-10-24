package com.topik.topikkorea.analyze.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_analyze")
public class MemberAnalyze extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.ORDINAL)
    @Column
    private ProblemType problemType;

    @Column
    private Integer correctCount;

    @Column
    private Integer totalCount;

    @Builder
    private MemberAnalyze(Member member, ProblemType problemType, Integer correctCount, Integer totalCount) {
        this.member = member;
        this.problemType = problemType;
        this.correctCount = correctCount;
        this.totalCount = totalCount;
    }

    public MemberAnalyze testMemberAnalyzeIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
