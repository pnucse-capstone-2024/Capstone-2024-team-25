CREATE TABLE gather
(
    id         bigint PRIMARY KEY NOT NULL,
    name       varchar(255),
    center     bigint             NOT NULL,
    member     bigint             NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE gather
    ADD CONSTRAINT fk_gather_center
        FOREIGN KEY (center) REFERENCES center (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE gather
    ADD CONSTRAINT fk_gather_member
        FOREIGN KEY (member) REFERENCES member (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;
