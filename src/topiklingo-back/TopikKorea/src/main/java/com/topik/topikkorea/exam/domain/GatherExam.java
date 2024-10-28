package com.topik.topikkorea.exam.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.gather.domain.Gather;
import jakarta.persistence.CascadeType;
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
@Table(name = "gather_exam")
public class GatherExam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "gather_id", nullable = false)
    private Gather gather;

    @Column
    private int memberCount;

    @Column
    private Long sum;

    @Builder
    public GatherExam(Exam exam, Gather gather, int memberCount, Long sum) {
        this.exam = exam;
        this.gather = gather;
        this.memberCount = memberCount;
        this.sum = sum;
    }

    public void updateGatherExam(int score) {
        this.memberCount++;
        this.sum += score;
    }

    public GatherExam testGatherExamIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
