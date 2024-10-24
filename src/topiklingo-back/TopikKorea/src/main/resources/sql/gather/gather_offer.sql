CREATE TABLE gather_offer
(
    id         bigint PRIMARY KEY NOT NULL,
    gather_id  bigint             NOT NULL,
    member_id  bigint             NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE gather_offer
    ADD CONSTRAINT fk_gather_offer_gather
        FOREIGN KEY (gather_id) REFERENCES gather (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE gather_offer
    ADD CONSTRAINT fk_gather_offer_member
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
