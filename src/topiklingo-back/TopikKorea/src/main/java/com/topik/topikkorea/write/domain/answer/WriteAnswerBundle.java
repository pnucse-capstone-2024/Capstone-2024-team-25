package com.topik.topikkorea.write.domain.answer;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class WriteAnswerBundle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "bundle")
    private List<WriteAnswer> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private boolean isGraduated;

    @Builder
    public WriteAnswerBundle(Member member) {
        this.member = member;
        this.isGraduated = false;
    }

    public void updateIsGraduated(boolean isGraduated) {
        this.isGraduated = isGraduated;
    }
}
