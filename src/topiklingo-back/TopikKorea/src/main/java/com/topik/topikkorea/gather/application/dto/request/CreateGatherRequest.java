package com.topik.topikkorea.gather.application.dto.request;

import lombok.Builder;

@Builder
public record CreateGatherRequest(
        String name
) {
}
