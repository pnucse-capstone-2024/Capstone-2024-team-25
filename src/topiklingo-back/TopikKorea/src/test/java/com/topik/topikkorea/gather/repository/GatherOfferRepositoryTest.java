package com.topik.topikkorea.gather.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.repository.CenterRepository;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.gather.domain.repository.GatherOfferRepository;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
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
public class GatherOfferRepositoryTest {
    @Autowired
    private GatherOfferRepository gatherOfferRepository;

    @Autowired
    private GatherRepository gatherRepository;

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Gather gather;

    private Center center;

    private Member teacher;

    private Member student;

    private GatherOffer gatherOffer;

    @BeforeEach
    @Transactional
    public void init() {
        center = CenterFixture.testCenter();
        center = centerRepository.save(center);

        teacher = MemberFixture.testMember(AuthType.TEACHER, LoginProvider.GOOGLE.name());
        teacher.updateMemberCenter(center);
        teacher = memberRepository.save(teacher);

        student = MemberFixture.testMember(AuthType.STUDENT, LoginProvider.GOOGLE.name());
        student.updateMemberCenter(center);
        student = memberRepository.save(student);

        gather = GatherFixture.testGather(center, teacher);
        gather = gatherRepository.save(gather);

        gatherOffer = GatherOffer.builder()
                .gather(gather)
                .member(student)
                .build();
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 제안 생성 성공")
    public void insertGatherOffer_그룹_제안_생성_성공() {
        // when
        final GatherOffer savedGatherOffer = gatherOfferRepository.save(GatherOffer.builder()
                .gather(gather)
                .member(teacher)
                .build());

        // then
        assertThat(savedGatherOffer.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 제안 조회 성공")
    public void getGatherOffer_그룹_제안_조회_성공() {
        // given
        final GatherOffer savedGatherOffer = gatherOfferRepository.save(gatherOffer);

        // when
        final GatherOffer foundGatherOffer = gatherOfferRepository.findById(savedGatherOffer.getId()).get();

        // then
        assertThat(foundGatherOffer).isEqualTo(savedGatherOffer);
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 제안 삭제 성공")
    public void deleteGatherOffer_그룹_제안_삭제_성공() {
        // given
        final GatherOffer savedGatherOffer = gatherOfferRepository.save(gatherOffer);

        // when
        gatherOfferRepository.deleteById(savedGatherOffer.getId());

        // then
        assertThat(gatherOfferRepository.findById(savedGatherOffer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @DisplayName("[success] 그룹 아이디로 그룹 제안 조회 성공")
    public void getGatherOfferByGatherId_그룹_아이디로_그룹_제안_조회_성공() {
        // given
        GatherOffer gatherOffer2 = GatherOffer.builder()
                .gather(gather)
                .member(teacher)
                .build();
        gatherOfferRepository.save(gatherOffer);
        gatherOfferRepository.save(gatherOffer2);

        // when
        final List<GatherOffer> foundGatherOffers = gatherOfferRepository.findGatherOffersByGather(gather).get();

        // then
        assertThat(foundGatherOffers.size()).isEqualTo(2);
    }
}
