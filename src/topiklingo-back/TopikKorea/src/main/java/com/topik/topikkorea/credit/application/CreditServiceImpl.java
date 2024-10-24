package com.topik.topikkorea.credit.application;

import com.topik.topikkorea.credit.application.dto.request.GetCreditsRequest;
import com.topik.topikkorea.credit.application.dto.request.GiveCreditRequest;
import com.topik.topikkorea.credit.application.dto.request.UseCreditRequest;
import com.topik.topikkorea.credit.application.dto.response.UnusedCreditResponse;
import com.topik.topikkorea.credit.application.dto.response.UsedCreditResponse;
import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.credit.domain.repository.CreditRepository;
import com.topik.topikkorea.credit.exception.CreditException;
import com.topik.topikkorea.credit.exception.CreditExceptionType;
import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.application.MemberService;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    @Value("${master.id}")
    private String masterId;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CreditRepository creditRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void DailyCredits() {
        List<Member> students = memberService.getAllStudents();
        List<Credit> giveCredits = new java.util.ArrayList<>(List.of());

        students.forEach(student -> {
            List<Credit> credits = creditRepository.findUnusedCredits(student.getId());
            if (credits.size() < 3) {
                for (int i = 0; i < 3 - credits.size(); i++) {
                    Credit credit = Credit.builder()
                            .creditor(memberService.getMember(Long.parseLong(masterId)))
                            .receiver(student)
                            .used(false)
                            .build();
                    giveCredits.add(credit);
                }
            }
        });

        creditRepository.saveAll(giveCredits);
    }

    @AuthCheck({AuthType.ADMIN, AuthType.CENTER})
    @Override
    public void giveCredits(GiveCreditRequest request) {
        List<Credit> credits = IntStream.range(0, request.credit())
                .mapToObj(i -> Credit.builder()
                        .creditor(request.creditor())
                        .receiver(memberService.getMember(request.receiverId()))
                        .used(false)
                        .build())
                .toList();

        creditRepository.saveAll(credits);
    }

    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @Override
    public List<UnusedCreditResponse> getUnusedCredit(GetCreditsRequest request) {
        return UnusedCreditResponse.of(creditRepository.findUnusedCredits(request.member().getId()));
    }

    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @Override
    public List<UsedCreditResponse> getUsedCredit(GetCreditsRequest request) {
        return UsedCreditResponse.of(creditRepository.findUsedCredits(request.member().getId()));
    }

    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @Override
    public void useCredit(UseCreditRequest request) {
        Credit credit = creditRepository.findOldestCredit(request.member().getId())
                .orElseThrow(() -> new CreditException(CreditExceptionType.GET_UNUSED_CREDITS_ERROR));
        credit.useCredit(request.exam());
        creditRepository.save(credit);
    }
}
