CREATE TABLE center_offer
(
    id         bigint NOT NULL,
    center_id  bigint NOT NULL,
    member_id  bigint NOT NULL,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);

ALTER TABLE center_offer
    ADD CONSTRAINT fk_center_offer_center
        FOREIGN KEY (center_id) REFERENCES center (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE center_offer
    ADD CONSTRAINT fk_center_offer_member
        FOREIGN KEY (member_id) REFERENCES member (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;