CREATE TABLE center
(
    id         bigint NOT NULL,
    address    varchar(255),
    name       varchar(255),
    nation     varchar(255),
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    is_deleted boolean
);