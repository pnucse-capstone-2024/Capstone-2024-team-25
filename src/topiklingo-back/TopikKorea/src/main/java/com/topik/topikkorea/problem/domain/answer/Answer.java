package com.topik.topikkorea.problem.domain.answer;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.problem.domain.question.Question;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    private String id;

    @Column
    private AnswerType AType;

    @Setter
    @Column
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Builder
    public Answer(String uuid, String AType, String answer, Question question) {
        this.id = uuid;
        this.AType = AnswerType.valueOf(AType.toUpperCase());
        this.answer = answer;
        this.question = question;
    }

    public void update(String answer) {
        this.answer = answer;
    }
}
