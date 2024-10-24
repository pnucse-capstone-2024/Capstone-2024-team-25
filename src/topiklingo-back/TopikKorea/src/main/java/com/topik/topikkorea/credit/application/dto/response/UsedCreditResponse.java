package com.topik.topikkorea.credit.application.dto.response;

import com.topik.topikkorea.credit.domain.Credit;
import com.topik.topikkorea.exam.application.dto.response.ExamIdResponse;
import com.topik.topikkorea.member.application.dto.response.CreditMemberResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record UsedCreditResponse(
        Long id,
        CreditMemberResponse creditor,
        CreditMemberResponse receiver,
        boolean used,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ExamIdResponse exam
) {
    public static List<UsedCreditResponse> of(List<Credit> credits) {
        return credits.stream()
                .map(credit -> UsedCreditResponse.builder()
                        .id(credit.getId())
                        .creditor(CreditMemberResponse.of(credit.getCreditor()))
                        .receiver(CreditMemberResponse.of(credit.getReceiver()))
                        .used(credit.isUsed())
                        .exam(ExamIdResponse.builder()
                                .examId(credit.getExam().getId())
                                .build()
                        )
                        .createdAt(credit.getCreatedAt())
                        .updatedAt(credit.getUpdatedAt())
                        .build()
                ).collect(Collectors.toList());
    }
}
