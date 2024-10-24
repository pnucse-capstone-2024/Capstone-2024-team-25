package com.topik.topikkorea.gather.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "gather")
public class Gather extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center", nullable = false)
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member", nullable = false)
    private Member teacher;

    @OneToMany(mappedBy = "gather", cascade = CascadeType.REMOVE)
    private List<GatherOffer> gatherOffers;

    @OneToMany(mappedBy = "gather")
    private List<Member> members;

    @Builder
    private Gather(String name, Center center, Member teacher) {
        this.name = name;
        this.center = center;
        this.teacher = teacher;
    }

    public void update(String name, Center center, Member teacher) {
        this.name = name;
        this.center = center;
        this.teacher = teacher;
    }

    public Gather testGatherIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
