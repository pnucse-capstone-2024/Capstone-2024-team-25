CREATE TABLE answer
(
    id          varchar(255) PRIMARY KEY NOT NULL,
    atype       smallint,
    answer      varchar(255),
    question_id varchar(255)             NOT NULL,
    created_at  timestamp(6) with time zone,
    updated_at  timestamp(6) with time zone,
    CONSTRAINT answer_atype_check CHECK (((atype >= 0) AND (atype <= 1)))
);

ALTER TABLE answer
    ADD CONSTRAINT fk_answer_question
        FOREIGN KEY (question_id) REFERENCES question (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;