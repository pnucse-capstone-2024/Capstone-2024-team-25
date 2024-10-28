package com.topik.topikkorea.credit.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.credit.application.dto.request.GetCreditsRequest;
import com.topik.topikkorea.credit.application.dto.request.GiveCreditRequest;
import com.topik.topikkorea.credit.application.dto.response.UnusedCreditResponse;
import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.credit.domain.repository.CreditRepository;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.application.MemberService;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    @DisplayName("[success] daily credit 부여 성공")
    public void daily_credit_부여_성공() {
        // given
        Member creditor = MemberFixture.testIdMember(null, AuthType.ADMIN, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);
        Member receiver = MemberFixture.testIdMember(11L, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);
        Member receiver2 = MemberFixture.testIdMember(12L, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);

        List<Member> students = List.of(receiver, receiver2);

        // mocking
        when(memberService.getAllStudents()).thenReturn(students);
        when(memberService.getMember(6L)).thenReturn(creditor);

        // when
        creditService.DailyCredits();

        // then
        verify(memberService).getAllStudents();
        verify(creditRepository).saveAll(anyList());
    }


    @Test
    @DisplayName("[success] credit 부여 성공")
    public void credit_부여_성공() {
        // given
        Member creditor = MemberFixture.testIdMember(null, AuthType.ADMIN, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);
        Member receiver = MemberFixture.testIdMember(11L, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);

        GiveCreditRequest request = GiveCreditRequest.builder()
                .creditor(creditor)
                .receiverId(receiver.getId())
                .credit(10)
                .build();

        // mocking
        when(memberService.getMember(request.receiverId())).thenReturn(receiver);

        // when
        creditService.giveCredits(request);

        // then
        verify(memberService, times(10)).getMember(request.receiverId());
        verify(creditRepository).saveAll(anyList());
    }

    @Test
    @DisplayName("[success] 사용하지 않은 credit 조회")
    public void 사용하지_않은_credit_조회() {
        // given
        Member creditor = MemberFixture.testIdMember(null, AuthType.ADMIN, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);
        Member receiver = MemberFixture.testIdMember(11L, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE,
                null, null);

        GetCreditsRequest request = GetCreditsRequest.builder()
                .member(receiver)
                .build();

        List<Credit> credits = IntStream.range(0, 10)
                .mapToObj(i -> Credit.builder()
                        .creditor(creditor)
                        .receiver(receiver)
                        .used(false)
                        .build())
                .toList();

        List<UnusedCreditResponse> responses = UnusedCreditResponse.of(credits);
        log.info("responses : {}", responses);

        // mocking
        when(creditRepository.findUnusedCredits(receiver.getId())).thenReturn(credits);

        // when
        List<UnusedCreditResponse> result = creditService.getUnusedCredit(request);

        // then
        assertThat(result).isEqualTo(responses);
    }
}
