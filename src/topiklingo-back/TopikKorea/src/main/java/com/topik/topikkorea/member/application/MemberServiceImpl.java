package com.topik.topikkorea.member.application;

import com.topik.topikkorea.center.application.CenterService;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.center.domain.CenterOffer;
import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.credit.domain.repository.CreditRepository;
import com.topik.topikkorea.gather.application.GatherService;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.gather.domain.GatherOffer;
import com.topik.topikkorea.member.application.dto.request.GoogleLoginRequest;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.application.dto.response.GoogleAccountProfileResponse;
import com.topik.topikkorea.member.application.dto.response.LoginResponse;
import com.topik.topikkorea.member.application.dto.response.MemberInfoResponse;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import com.topik.topikkorea.member.utils.jwt.JwtTokenProvider;
import com.topik.topikkorea.member.utils.provider.google.GoogleClient;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    @Value("${master.id}")
    private String masterId;

    private final GoogleClient googleClient;

    private final CenterService centerService;

    private final GatherService gatherService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final CreditRepository creditRepository;

    @Override
    public LoginResponse loginByGoogle(GoogleLoginRequest request) {
        final GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(request.code());
        final Member member = findOrCreateMemberByGoogle(profile);
        final String token = jwtTokenProvider.generateToken(member);
        return new LoginResponse(member.getId(), member.getAuthType().name().toUpperCase(), token);

    }

    @Transactional
    @Override
    public void insertMemberDetail(Long memberId, MemberDetailRequest request) {
        final Member member = getMember(memberId);
        member.updateMemberDetail(request);
        memberRepository.save(member);

        List<Credit> credits = IntStream.range(0, 3)
                .mapToObj(i -> Credit.builder()
                        .creditor(getMember(Long.parseLong(masterId)))
                        .receiver(getMember(memberId))
                        .used(false)
                        .build())
                .toList();

        creditRepository.saveAll(credits);
    }

    @Override
    @Transactional
    public void updateMemberAuth(String email, String authType) {
        memberRepository.updateAuthTypeByEmail(email, AuthType.valueOf(authType.toUpperCase()));
    }

    @Override
    public List<Member> getAllStudents() {
        return memberRepository.findAllStudents();
    }

    // 멤버에 센터 정보를 주입한다.
    @Override
    @Transactional
    public void insertMemberCenter(Long centerOfferId, Member member) {
        final CenterOffer centerOffer = centerService.getCenterOffer(centerOfferId);
        final Center center = centerOffer.getCenter();
        final Member centerMember = centerOffer.getMember();
        if (center != member.getCenter()) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }
        centerMember.updateMemberCenter(center);
        centerService.deleteOfferCenter(centerOfferId);
        memberRepository.save(centerMember);
    }

    @Override
    @Transactional
    public void insertMemberGather(Long groupOfferId, Member member) {
        final GatherOffer gatherOffer = gatherService.getGatherOffer(groupOfferId);
        final Gather gather = gatherOffer.getGather();
        final Member gatherMember = gatherOffer.getMember();

        if (gather.getCenter() != member.getCenter()) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }
        gatherMember.updateMemberGather(gather);
        gatherService.deleteOfferGather(groupOfferId);
        memberRepository.save(gatherMember);
    }

    @Override
    public List<Member> getGatherMembers(Long gatherId) {
        return memberRepository.findByGatherId(gatherId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_GROUP_MEMBER));
    }

    @Override
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN));
    }

    @Override
    public MemberInfoResponse getMemberInfo(Long memberId, Member member) {
        final Member findMember = getMember(memberId);
        if (findMember != member) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }

        return MemberInfoResponse.builder()
                .id(findMember.getId())
                .name(findMember.getName())
                .email(findMember.getEmail())
                .nation(findMember.getNation())
                .gender(findMember.getGender().name())
                .birth(findMember.getBirth().toString())
                .provider(findMember.getProvider())
                .center(findMember.getCenter() != null ? findMember.getCenter().getName() : "None") // 생략 가능
                .gather(findMember.getGather() != null ? findMember.getGather().getName() : "None")
                .build();
    }

    private Member findOrCreateMemberByGoogle(final GoogleAccountProfileResponse profile) {
        return memberRepository.findMemberByProviderAndProviderId(LoginProvider.GOOGLE.name(), profile.id())
                .orElseGet(() -> memberRepository.save(
                                Member.builder()
                                        .name(profile.name())
                                        .email(profile.email())
                                        .authType(AuthType.UNVERIFIED_USER)
                                        .provider(LoginProvider.GOOGLE.name())
                                        .providerId(profile.id())
                                        .isDeleted(false)
                                        .build()
                        )
                );
    }
}
