package com.topik.topikkorea.center.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.center.domain.repository.CenterOfferRepository;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CenterOfferRepositoryTest {
    @Autowired
    private CenterOfferRepository centerOfferRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Center center;

    private Member member;

    private CenterOffer centerOffer;

    @BeforeEach
    public void init() {
        center = CenterFixture.testCenter();
        center = centerRepository.save(center);

        member = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        member = memberRepository.save(member);

        centerOffer = CenterOffer.builder()
                .center(center)
                .member(member)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 오퍼 저장")
    public void insertCenterOffer_센터_오퍼_저장() {
        // when
        CenterOffer savedCenterOffer = centerOfferRepository.save(centerOffer);

        // then
        assertThat(savedCenterOffer.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 오퍼 조회")
    public void findCenterOfferById_센터_오퍼_조회() {
        // given
        CenterOffer savedCenterOffer = centerOfferRepository.save(centerOffer);

        // when
        CenterOffer findCenterOffer = centerOfferRepository.findById(savedCenterOffer.getId()).get();

        // then
        assertThat(findCenterOffer).isEqualTo(savedCenterOffer);
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 오퍼 삭제")
    public void deleteCenterOffer_센터_오퍼_삭제() {
        // given
        CenterOffer savedCenterOffer = centerOfferRepository.save(centerOffer);

        // when
        centerOfferRepository.deleteById(savedCenterOffer.getId());

        // then
        assertThat(centerOfferRepository.findById(savedCenterOffer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 오퍼 리스트 조회")
    public void findCenterOffers_센터_오퍼_리스트_조회() {
        // given
        centerOfferRepository.save(centerOffer);
        CenterOffer centerOffer2 = CenterOffer.builder()
                .center(center)
                .member(member)
                .build();
        centerOfferRepository.save(centerOffer2);

        // when
        List<CenterOffer> centerOffers = centerOfferRepository.findCenterOffersByCenter(center.getId()).get();

        // then
        assertThat(centerOffers.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("[success] 삭제된 센터 오퍼 리스트 조회시 빈 리스트 반환")
    public void findCenterOffers_삭제된_센터_오퍼_리스트_조회() {
        // given
        centerOfferRepository.save(centerOffer);
        CenterOffer centerOffer2 = CenterOffer.builder()
                .center(center)
                .member(member)
                .build();
        centerOfferRepository.save(centerOffer2);
        centerRepository.deleteById(center.getId());

        // when
        List<CenterOffer> centerOffers = centerOfferRepository.findCenterOffersByCenter(center.getId()).get();

        // when & then
        assertThat(centerOffers.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @DisplayName("[success] 삭제된 멤버 오퍼 리스트 조회시 빈 리스트 반환")
    public void findCenterOffer_삭제된_멤버_오퍼_리스트_조회() {
        // given
        centerOfferRepository.save(centerOffer);
        memberRepository.deleteById(member.getId());

        // then
        assertThat(centerOfferRepository.findCenterOfferByMember(member.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 센터 오퍼 멤버 조회")
    public void findCenterOfferByMember_센터_오퍼_조회() {
        // given
        CenterOffer savedCenterOffer = centerOfferRepository.save(centerOffer);

        // when
        CenterOffer centerOffer = centerOfferRepository.findCenterOfferByMember(member.getId()).get();

        // then
        assertThat(centerOffer).isEqualTo(savedCenterOffer);
    }
}
