package com.topik.topikkorea.write.domain.problem;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.exam.domain.ExamWriteProblem;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WriteProblem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String problem;

    @Column
    private WriteProblemType WType;

    @OneToMany(mappedBy = "problem")
    private List<WriteQuestion> questions;

    @OneToMany(mappedBy = "problem")
    private List<ExamWriteProblem> examWriteProblems;

    @Builder
    public WriteProblem(String problem, WriteProblemType WType) {
        this.problem = problem;
        this.WType = WType;
    }

    public WriteProblem testWriteProblemIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
