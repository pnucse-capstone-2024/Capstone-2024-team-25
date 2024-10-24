package com.topik.topikkorea.write.domain.question;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WriteQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String question;

    @Column(columnDefinition = "TEXT")
    String example;

    @Column
    int score;

    @OneToMany(mappedBy = "question")
    List<WriteAnswer> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem", nullable = false)
    WriteProblem problem;

    @Builder
    public WriteQuestion(String question, String example, int score, WriteProblem problem) {
        this.question = question;
        this.example = example;
        this.score = score;
        this.problem = problem;
    }

    public WriteQuestion testWriteQuestionIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
