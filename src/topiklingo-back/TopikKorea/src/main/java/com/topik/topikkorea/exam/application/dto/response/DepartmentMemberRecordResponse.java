package com.topik.topikkorea.exam.application.dto.response;

import java.time.LocalDateTime;

public record DepartmentMemberRecordResponse(
        String department,
        String name,
        String email,
        String examName,
        int score,
        LocalDateTime date
) {
}
