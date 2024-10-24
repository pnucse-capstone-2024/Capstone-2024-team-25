package com.topik.topikkorea.exam.application.dto.request;

public record DepartmentRecordRequest(
        String department,
        String startDate,
        String endDate
) {
}
