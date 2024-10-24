package com.topik.topikkorea.problem.domain.problem;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.exam.domain.ExamProblem;
import com.topik.topikkorea.problem.domain.question.Question;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Table(name = "problem")
public class Problem extends BaseEntity {
    @Id
    private String id;

    @Column
    private String problem;

    @Column
    private ProblemType PType;


    @Setter
    @Column(columnDefinition = "TEXT")
    private String example;

    @Column
    private ExampleType EType;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.REMOVE)
    private List<Question> questions;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.REMOVE)
    private List<ExamProblem> examProblems;

    @Builder
    private Problem(String uuid, String PType, String problem, String EType, String example) {
        this.id = uuid;
        this.PType = ProblemType.valueOf(PType.toUpperCase());
        this.problem = problem;
        this.EType = ExampleType.valueOf(EType.toUpperCase());
        this.example = example;
    }

    public void updateProblem(String example) {
        this.example = example;
    }
}
