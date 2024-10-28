package com.topik.topikkorea.center.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "center")
public class Center extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String nation;

    @Column
    private String address;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "center", cascade = CascadeType.REMOVE)
    private List<CenterOffer> centerOffers;

    @OneToMany(mappedBy = "center", cascade = CascadeType.REMOVE)
    private List<Gather> gathers;

    @OneToMany(mappedBy = "center")
    private List<Member> members;

    @Builder
    private Center(String name, String nation, String address) {
        this.name = name;
        this.nation = nation;
        this.address = address;
        this.isDeleted = false;
    }

    public void update(String name, String nation, String address) {
        this.name = name;
        this.nation = nation;
        this.address = address;
    }

    public Center testCenterIdSetting(Long id) {
        this.id = id;
        return this;
    }
}
