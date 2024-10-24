package com.topik.topikkorea.credit.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Credit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor", nullable = false)
    private Member creditor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver", nullable = false)
    private Member receiver;

    @Column
    private boolean used;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam")
    private Exam exam;

    @Builder
    public Credit(Member creditor, Member receiver, boolean used) {
        this.creditor = creditor;
        this.receiver = receiver;
        this.used = used;
    }

    public void useCredit(Exam exam) {
        this.exam = exam;
        this.used = true;
    }
}
