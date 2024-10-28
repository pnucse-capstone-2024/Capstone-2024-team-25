CREATE TABLE member_exam
(
    id             bigint PRIMARY KEY NOT NULL,
    member_answers varchar(255),
    real_answers   varchar(255),
    score          integer,
    exam_id        varchar(255)       NOT NULL,
    member_id      bigint             NOT NULL,
    created_at     timestamp(6) with time zone,
    updated_at     timestamp(6) with time zone
);

ALTER TABLE member_exam
    ADD CONSTRAINT fk_member_exam_exam
        FOREIGN KEY (exam_id) REFERENCES exam (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE member_exam
    ADD CONSTRAINT fk_member_exam_member
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;