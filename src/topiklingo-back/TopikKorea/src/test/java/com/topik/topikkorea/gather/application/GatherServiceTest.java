package com.topik.topikkorea.gather.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.gather.application.dto.request.CreateGatherRequest;
import com.topik.topikkorea.gather.application.dto.request.OfferGatherRequest;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.gather.domain.repository.GatherOfferRepository;
import com.topik.topikkorea.gather.domain.repository.GatherRepository;
import com.topik.topikkorea.gather.exception.GatherException;
import com.topik.topikkorea.gather.exception.GatherExceptionType;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GatherServiceTest {
    @Mock
    private GatherRepository gatherRepository;

    @Mock
    private GatherOfferRepository gatherOfferRepository;

    @InjectMocks
    private GatherServiceImpl gatherService;

    private Center center;

    private Gather gather;

    private GatherOffer gatherOffer;

    private Member teacher;

    private Member student;

    @BeforeEach
    public void init() {
        center = CenterFixture.testIdCenter();
        teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.FEMALE, center,
                null);
        student = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE, center,
                null);
        gather = GatherFixture.testIdGather(center, teacher);
        gatherOffer = GatherOffer.builder()
                .gather(gather)
                .member(student)
                .build();
        gatherOffer.testGatherOfferIdSetting(1L);
    }

    @Test
    @DisplayName("[success] 그룹 업데이트 성공")
    public void updateGather_그룹_업데이트_성공() {
        // given
        String updatedName = "updatedName";
        CreateGatherRequest request = CreateGatherRequest.builder()
                .name(updatedName)
                .build();

        // mocking
        when(gatherRepository.findById(center.getId())).thenReturn(java.util.Optional.of(gather));

        // when
        gatherService.updateGather(center.getId(), request, teacher);

        // then
        assertThat(gather.getName()).isEqualTo(updatedName);
    }

    @Test
    @DisplayName("[fail] 그룹 조회 실패")
    public void getGather_그룹_조회_실패() {
        // mocking
        when(gatherRepository.findById(gather.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gatherService.getGather(gather.getId()))
                .isInstanceOf(GatherException.class)
                .hasMessageContaining(GatherExceptionType.NOT_FOUND_GROUP.errorMessage());
    }

    @Test
    @DisplayName("[fail] 그룹 오퍼 중복 실패")
    public void offerGather_그룹_오퍼_중복_실패() {
        // given
        OfferGatherRequest request = OfferGatherRequest.builder()
                .gatherId(gather.getId())
                .build();

        // mocking
        when(gatherOfferRepository.findGatherOffersByMember(student)).thenReturn(Optional.of(gatherOffer));

        // when & then
        assertThatThrownBy(() -> gatherService.offerGather(request, student))
                .isInstanceOf(GatherException.class)
                .hasMessageContaining(GatherExceptionType.ALREADY_EXIST_GROUP_OFFER.errorMessage());
    }

    @Test
    @DisplayName("[fail] 그룹 오퍼 조회 실패")
    public void getGatherOffer_그룹_오퍼_조회_실패() {
        // mocking
        when(gatherOfferRepository.findById(gatherOffer.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gatherService.getGatherOffer(gatherOffer.getId()))
                .isInstanceOf(GatherException.class)
                .hasMessageContaining(GatherExceptionType.NOT_FOUND_GROUP_OFFER.errorMessage());
    }
}
