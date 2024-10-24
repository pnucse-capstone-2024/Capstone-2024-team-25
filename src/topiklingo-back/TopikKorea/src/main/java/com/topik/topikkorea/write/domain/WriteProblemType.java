package com.topik.topikkorea.write.domain;

public enum WriteProblemType {
    WRITING_2_PROBLEM_TYPE_1,
    WRITING_2_PROBLEM_TYPE_2,
    WRITING_2_PROBLEM_TYPE_3;

    public static WriteProblemType from(String type) {
        return WriteProblemType.valueOf(type);
    }
}
