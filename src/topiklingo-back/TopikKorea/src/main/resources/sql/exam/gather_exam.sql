CREATE TABLE gather_exam
(
    id           bigint       NOT NULL,
    member_count integer,
    sum          bigint,
    exam_id      varchar(255) NOT NULL,
    gather_id    bigint       NOT NULL,
    created_at   timestamp(6) with time zone,
    updated_at   timestamp(6) with time zone
);

ALTER TABLE gather_exam
    ADD CONSTRAINT fk_gather_exam_exam
        FOREIGN KEY (exam_id) REFERENCES exam (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE gather_exam
    ADD CONSTRAINT fk_gather_exam_gather
        FOREIGN KEY (gather_id) REFERENCES gather (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;