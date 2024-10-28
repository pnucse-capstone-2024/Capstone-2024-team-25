package com.topik.topikkorea.write.domain.answer;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
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
public class WriteAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int score;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question", nullable = false)
    private WriteQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle", nullable = false)
    WriteAnswerBundle bundle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", nullable = false)
    private Member member;

    @Builder
    public WriteAnswer(String answer, WriteQuestion question, Member member, WriteAnswerBundle bundle) {
        this.answer = answer;
        this.question = question;
        this.member = member;
        this.bundle = bundle;
    }

    public void gradingAnswer(int score, String reason) {
        this.score = score;
        this.reason = reason;
    }

    public WriteAnswer testWriteAnswerIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
