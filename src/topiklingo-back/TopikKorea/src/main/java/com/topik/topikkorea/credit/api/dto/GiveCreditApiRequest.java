package com.topik.topikkorea.credit.api.dto;

public record GiveCreditApiRequest(
        Long receiverId,
        int credit
) {
}
