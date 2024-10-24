CREATE TABLE exam_problem
(
    id         uuid PRIMARY KEY NOT NULL,
    exam_id    varchar(255)     NOT NULL,
    problem_id varchar(255)     NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE exam_problem
    ADD CONSTRAINT fk_exam_problem_exam
        FOREIGN KEY (exam_id) REFERENCES exam (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE exam_problem
    ADD CONSTRAINT fk_exam_problem_problem
        FOREIGN KEY (problem_id) REFERENCES problem (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;