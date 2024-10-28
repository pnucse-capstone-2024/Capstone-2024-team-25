package com.topik.topikkorea.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.center.application.CenterService;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.gather.application.GatherService;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.application.dto.request.GoogleLoginRequest;
import com.topik.topikkorea.member.application.dto.response.GoogleAccountProfileResponse;
import com.topik.topikkorea.member.application.dto.response.LoginResponse;
import com.topik.topikkorea.member.application.dto.response.MemberInfoResponse;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import com.topik.topikkorea.member.utils.jwt.JwtTokenProvider;
import com.topik.topikkorea.member.utils.provider.google.GoogleClient;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private GoogleClient googleClient;

    @Mock
    private CenterService centerService;

    @Mock
    private GatherService gatherService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Center center;

    private Gather gather;

    private Member member;

    private Member teacher;

    private GoogleAccountProfileResponse profile;

    @BeforeEach
    public void init() {
        center = CenterFixture.testIdCenter();
        teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.FEMALE, center,
                null);
        gather = GatherFixture.testIdGather(center, teacher);
        member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE, center,
                gather);

        profile = GoogleAccountProfileResponse.builder()
                .id(member.getProviderId())
                .email(member.getEmail())
                .verifiedEmail(true)
                .name(member.getName())
                .givenName("testGivenName")
                .familyName("testFamilyName")
                .picture("testPicture")
                .locale("testLocale")
                .build();
    }

    @Test
    @DisplayName("[success] 존재 유저 구글 로그인 성공")
    public void loginByGoogle_존재_유저_구글_로그인_성공() {
        // given
        String googleCode = "testCode";
        String jwtToken = "testToken";
        LoginResponse answer = new LoginResponse(member.getId(), member.getAuthType().name().toUpperCase(), jwtToken);

        // mocking
        when(googleClient.getGoogleAccountProfile(googleCode))
                .thenReturn(profile);
        when(memberRepository.findMemberByProviderAndProviderId(LoginProvider.GOOGLE.name(), member.getProviderId()))
                .thenReturn(Optional.of(member));
        when(jwtTokenProvider.generateToken(member))
                .thenReturn(jwtToken);

        // when
        GoogleLoginRequest request = new GoogleLoginRequest(googleCode);
        LoginResponse loginResponse = memberService.loginByGoogle(request);
        // then
        assertThat(loginResponse).isEqualTo(answer);
    }

    @Test
    @DisplayName("[success] 존재하지 않는 유저 구글 로그인 성공")
    public void loginByGoogle_존재하지_않는_유저_구글_로그인_성공() {
        // given
        String googleCode = "testCode";
        String jwtToken = "testToken";

        LoginResponse answer = new LoginResponse(member.getId(),
                member.getAuthType().name().toUpperCase(), jwtToken);

        // mocking
        when(googleClient.getGoogleAccountProfile(googleCode))
                .thenReturn(profile);
        when(memberRepository.findMemberByProviderAndProviderId(LoginProvider.GOOGLE.name(), member.getProviderId()))
                .thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class)))
                .thenReturn(member);
        when(jwtTokenProvider.generateToken(member))
                .thenReturn(jwtToken);

        // when
        GoogleLoginRequest request = new GoogleLoginRequest(googleCode);
        LoginResponse loginResponse = memberService.loginByGoogle(request);
        // then
        assertThat(loginResponse).isEqualTo(answer);
    }

    @Test
    @DisplayName("[success] 멤버 센터 정보 주입 성공")
    public void insertMemberCenter_멤버_센터_정보_주입_성공() {
        // given
        Long centerOfferId = 1L;
        Member unMatchedMember = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(),
                Gender.FEMALE, null, null);

        CenterOffer centerOffer = CenterOffer.builder()
                .center(center)
                .member(unMatchedMember)
                .build();

        unMatchedMember.updateMemberCenter(center);

        // mocking
        when(centerService.getCenterOffer(centerOfferId))
                .thenReturn(centerOffer);
        doNothing().when(centerService)
                .deleteOfferCenter(centerOfferId);
        when(memberRepository.save(centerOffer.getMember()))
                .thenReturn(unMatchedMember);

        // when
        memberService.insertMemberCenter(centerOfferId, member);

        // then
        assertThat(unMatchedMember.getCenter()).isEqualTo(center);
    }

    @Test
    @DisplayName("[success] 멤버 모임 정보 주입 성공")
    public void insertMemberGather_멤버_모임_정보_주입_성공() {
        // given
        Long gatherOfferId = 1L;
        Member unMatchedMember = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(),
                Gender.FEMALE, center, null);

        GatherOffer gatherOffer = GatherOffer.builder()
                .gather(gather)
                .member(unMatchedMember)
                .build();

        unMatchedMember.updateMemberGather(gather);

        // mocking
        when(gatherService.getGatherOffer(gatherOfferId))
                .thenReturn(gatherOffer);
        doNothing().when(gatherService).deleteOfferGather(gatherOfferId);
        when(memberRepository.save(gatherOffer.getMember()))
                .thenReturn(unMatchedMember);

        // when
        memberService.insertMemberGather(gatherOfferId, member);

        // then
        assertThat(unMatchedMember.getGather()).isEqualTo(gather);
    }

    @Test
    @DisplayName("[fail] 모임 멤버 조회 실패")
    public void getGatherMembers_모임_멤버_조회_실패() {

        // mocking
        doThrow(new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION))
                .when(memberRepository)
                .findByGatherId(gather.getId());

        // when & then
        assertThatThrownBy(() -> memberService.getGatherMembers(gather.getId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.UNAUTHORIZED_PERMISSION.errorMessage());
    }

    @Test
    @DisplayName("[fail] 멤버 조회 실패")
    public void getMember_멤버_조회_실패() {
        // mocking
        doThrow(new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN))
                .when(memberRepository)
                .findById(member.getId());

        // when & then
        assertThatThrownBy(() -> memberService.getMember(member.getId()))
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.INVALID_ACCESS_TOKEN.errorMessage());
    }

    @Test
    @DisplayName("[fail] 멤버 정보 조회 실패")
    public void getMemberInfo_멤버_정보_조회_실패() {
        Member unMatchedMember = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(),
                Gender.FEMALE, center, null);

        // mocking
        when(memberRepository.findById(unMatchedMember.getId()))
                .thenReturn(Optional.of(unMatchedMember));

        // when & then
        assertThatThrownBy(() -> memberService.getMemberInfo(unMatchedMember.getId(), member))
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.UNAUTHORIZED_PERMISSION.errorMessage());
    }

    @Test
    @DisplayName("[success] 멤버 정보 조히 성공")
    public void getMemberInfo_멤버_정보_조회_성공() {
        // mocking
        when(memberRepository.findById(member.getId()))
                .thenReturn(Optional.of(member));

        // when
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(member.getId(), member);

        assertThat(memberInfoResponse.id()).isEqualTo(member.getId());
    }
}
