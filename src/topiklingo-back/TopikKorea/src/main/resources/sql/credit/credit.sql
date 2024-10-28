CREATE TABLE credit
(
    id         bigint                NOT NULL,
    creditor   bigint                NOT NULL,
    receiver   bigint                NOT NULL,
    used       boolean default false NOT NULL,
    exam       bigint,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE credit
    ADD CONSTRAINT fk_credit_creditor
        FOREIGN KEY (creditor) REFERENCES member (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

ALTER TABLE credit
    ADD CONSTRAINT fk_credit_receiver
        FOREIGN KEY (receiver) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE credit
    ADD CONSTRAINT fk_credit_exam
        FOREIGN KEY (exam) REFERENCES exam (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;