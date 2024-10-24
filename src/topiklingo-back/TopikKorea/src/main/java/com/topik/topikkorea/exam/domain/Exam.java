package com.topik.topikkorea.exam.domain;


import com.topik.topikkorea.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "exam")
public class Exam extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.REMOVE)
    private List<ExamProblem> examProblems;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.REMOVE)
    private List<ExamWriteProblem> examWriteProblems;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.REMOVE)
    private List<MemberExam> memberExams;

    @Setter
    @Column
    private String listenUrl;

    @Column
    private ExamType type;

    @Column
    private Integer year;

    @Column
    private boolean isDeleted;

    @Builder
    private Exam(String uuid, String title, String type, Integer year) {
        this.id = uuid;
        this.title = title;
        this.type = ExamType.valueOf(type.toUpperCase());
        this.year = year;
        this.isDeleted = false;
    }

    public void update(String title, String type, Integer year) {
        this.title = title;
        this.type = ExamType.valueOf(type.toUpperCase());
        this.year = year;
    }
}
