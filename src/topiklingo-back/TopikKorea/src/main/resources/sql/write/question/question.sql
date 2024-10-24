CREATE TABLE write_question
(
    id         bigint PRIMARY KEY NOT NULL,
    question   varchar(255),
    explain    text,
    score      int,
    problem    bigint             NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE write_question
    ADD CONSTRAINT fk_write_question_problem
        FOREIGN KEY (problem) REFERENCES write_problem (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

