package com.topik.topikkorea.exam.domain;

import com.topik.topikkorea.write.domain.problem.WriteProblem;
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
public class ExamWriteProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem", nullable = false)
    private WriteProblem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam", nullable = false)
    private Exam exam;

    @Builder
    private ExamWriteProblem(WriteProblem problem, Exam exam) {
        this.problem = problem;
        this.exam = exam;
    }
}
