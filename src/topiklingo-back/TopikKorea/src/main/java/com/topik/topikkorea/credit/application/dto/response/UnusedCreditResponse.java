package com.topik.topikkorea.credit.application.dto.response;

import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.member.application.dto.response.CreditMemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record UnusedCreditResponse(
        Long id,
        CreditMemberResponse creditor,
        CreditMemberResponse receiver,
        LocalDateTime createdAt,

        boolean used
) {
    public static List<UnusedCreditResponse> of(List<Credit> credits) {
        return credits.stream()
                .map(credit -> UnusedCreditResponse.builder()
                        .id(credit.getId())
                        .creditor(CreditMemberResponse.of(credit.getCreditor()))
                        .receiver(CreditMemberResponse.of(credit.getReceiver()))
                        .createdAt(credit.getCreatedAt())
                        .used(credit.isUsed())
                        .build()
                ).collect(Collectors.toList());
    }
}
