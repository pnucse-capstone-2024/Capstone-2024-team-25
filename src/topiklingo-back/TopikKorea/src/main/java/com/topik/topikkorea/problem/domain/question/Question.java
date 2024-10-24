package com.topik.topikkorea.problem.domain.question;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.problem.Problem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    private String id;

    @Column
    private QuestionExampleType QEType;

    @Column
    private String questionProblem;

    @Column
    private Integer score;

    @Column
    private Integer rightAnswer;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String example;

    @Column(columnDefinition = "TEXT")
    private String explain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @OrderBy("id")
    private List<Answer> answers;

    @Builder
    protected Question(String uuid, String QEType, String questionProblem, Integer score, Integer rightAnswer,
                       Problem problem, String explain, String example) {
        this.id = uuid;
        this.QEType = QuestionExampleType.valueOf(QEType.toUpperCase());
        this.questionProblem = questionProblem;
        this.score = score;
        this.rightAnswer = rightAnswer;
        this.explain = explain;
        this.example = example;
        this.problem = problem;
    }

    public void update(String questionProblem, String example) {
        this.questionProblem = questionProblem;
        this.example = example;
    }
}
