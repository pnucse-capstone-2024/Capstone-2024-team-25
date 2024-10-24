CREATE TABLE exam_problem
(
    id         bigint PRIMARY KEY NOT NULL,
    exam       varchar(255)       NOT NULL,
    problem    bigint             NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE exam_write_problem
    ADD CONSTRAINT fk_exam_write_problem_exam
        FOREIGN KEY (exam) REFERENCES exam (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE exam_write_problem
    ADD CONSTRAINT fk_exam_write_problem_problem
        FOREIGN KEY (problem) REFERENCES problem (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;