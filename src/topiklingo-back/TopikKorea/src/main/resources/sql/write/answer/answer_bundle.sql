CREATE TABLE write_answer_bundle
(
    id           bigint PRIMARY KEY NOT NULL,
    member       bigint             NOT NULL,
    is_graduated boolean default false,
    created_at   timestamp(6) with time zone,
    updated_at   timestamp(6) with time zone
);

ALTER TABLE write_answer_bundle
    ADD CONSTRAINT fk_write_answer_bundle_member
        FOREIGN KEY (member) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;