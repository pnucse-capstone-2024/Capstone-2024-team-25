package com.topik.topikkorea.center.application.dto.request;

public record CreateCenterRequest(
        String name,
        String nation,
        String address
) {
}
