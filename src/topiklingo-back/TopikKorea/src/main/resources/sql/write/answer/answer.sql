CREATE TABLE write_answer
(
    id         bigint PRIMARY KEY NOT NULL,
    answer     text,
    score      int,
    reason     text,
    question   bigint             NOT NULL,
    member     bigint             NOT NULL,
    bundle     bigint,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE write_answer
    ADD CONSTRAINT fk_write_answer_question
        FOREIGN KEY (question) REFERENCES write_question (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE write_answer
    ADD CONSTRAINT fk_write_answer_member
        FOREIGN KEY (member) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE write_answer
    ADD CONSTRAINT fk_write_answer_bundle
        FOREIGN KEY (bundle) REFERENCES write_answer_bundle (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
