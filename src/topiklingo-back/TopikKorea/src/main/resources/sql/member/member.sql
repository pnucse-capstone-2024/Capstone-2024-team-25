CREATE TABLE member
(
    id          bigint PRIMARY KEY NOT NULL,
    auth_type   smallint           NOT NULL,
    email       varchar(255)       NOT NULL,
    is_deleted  boolean            NOT NULL,
    name        varchar(255)       NOT NULL,
    nation      varchar(255),
    provider    varchar(255),
    provider_id varchar(255),
    birth       date,
    gender      smallint,
    department  varchar(255),
    center_id   bigint,
    gather_id   bigint,
    created_at  timestamp(6) with time zone,
    updated_at  timestamp(6) with time zone,
    CONSTRAINT member_auth_type_check CHECK (((auth_type >= 0) AND (auth_type <= 4))),
    CONSTRAINT member_gender_check CHECK (((gender >= 0) AND (gender <= 1)))
);

ALTER TABLE member
    ADD CONSTRAINT fk_member_center
        FOREIGN KEY (center_id) REFERENCES center (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

ALTER TABLE member
    ADD CONSTRAINT fk_member_gather
        FOREIGN KEY (gather_id) REFERENCES gather (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;