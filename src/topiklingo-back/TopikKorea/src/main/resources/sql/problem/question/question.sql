CREATE TABLE question
(
    id               varchar(255) PRIMARY KEY NOT NULL,
    qtype            smallint,
    explain          text,
    question_problem varchar(255),
    right_answer     integer,
    score            integer,
    problem_id       varchar(255)             NOT NULL,
    example          text,
    qetype           smallint,
    created_at       timestamp(6) with time zone,
    updated_at       timestamp(6) with time zone,
    CONSTRAINT question_qetype_check CHECK (((qetype >= 0) AND (qetype <= 1))),
    CONSTRAINT question_qtype_check CHECK (((qtype >= 0) AND (qtype <= 1)))
);

ALTER TABLE question
    ADD CONSTRAINT fk_question_problem
        FOREIGN KEY (problem_id) REFERENCES problem (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

