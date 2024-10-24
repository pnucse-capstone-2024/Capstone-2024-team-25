package com.topik.topikkorea.exam.domain;


import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "member_exam")
public class MemberExam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column
    private int score;

    @Column
    private String memberAnswers;

    @Column
    private String realAnswers;

    @Builder
    private MemberExam(Member member, Exam exam, int score, String memberAnswers, String realAnswers) {
        this.member = member;
        this.exam = exam;
        this.score = score;
        this.memberAnswers = memberAnswers;
        this.realAnswers = realAnswers;
    }

    public MemberExam testMemberExamIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
