package com.topik.topikkorea.member.domain;

import com.topik.topikkorea.base.BaseEntity;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
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
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String nation;

    @Column
    private Gender gender;

    @Column
    private LocalDate birth;

    @Column(nullable = false)
    private AuthType authType;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column
    private String department;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<CenterOffer> centerOffers;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<GatherOffer> gatherOffers;

    @OneToMany(mappedBy = "creditor", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Credit> creditsGiven;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Credit> creditsReceived;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<WriteAnswer> writeAnswers;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<WriteAnswerBundle> writeAnswerBundles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private Center center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gather_id")
    private Gather gather;

    @Builder
    private Member(String name, String email, AuthType authType, String provider, String providerId,
                   Boolean isDeleted) {
        this.name = name;
        this.email = email;
        this.authType = authType;
        this.provider = provider;
        this.providerId = providerId;
        this.isDeleted = isDeleted;
    }

    public void updateMemberDetail(MemberDetailRequest request) {
        this.nation = request.nation();
        this.gender = Gender.valueOf(request.gender());
        this.birth = LocalDate.parse(request.birth());
        this.department = request.department();
        this.authType = AuthType.STUDENT;
    }

    // Mocking을 위해 Id를 강제로 설정하는 메소드
    public Member testMemberIdSetting(long id) {
        this.id = id;
        return this;
    }

    public void updateMemberCenter(Center center) {
        this.center = center;
    }

    public void updateMemberGather(Gather gather) {
        this.gather = gather;
    }
}
