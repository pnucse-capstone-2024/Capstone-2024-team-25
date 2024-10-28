CREATE TABLE member_analyze
(
    id            bigint PRIMARY KEY NOT NULL,
    correct_count integer,
    problem_type  smallint,
    total_count   integer,
    member_id     bigint             NOT NULL,
    created_at    timestamp(6) with time zone,
    updated_at    timestamp(6) with time zone,
    CONSTRAINT member_analyze_problem_type_check CHECK (((problem_type >= 0) AND (problem_type <= 44)))
);

ALTER TABLE member_analyze
    ADD CONSTRAINT fk_member_analyze_member
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
