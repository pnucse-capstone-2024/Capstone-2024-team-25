CREATE TABLE exam
(
    id         varchar(255) PRIMARY KEY NOT NULL,
    title      varchar(255)             NOT NULL,
    listen_url varchar(255),
    type       smallint,
    year       integer,
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    is_deleted boolean,
    CONSTRAINT exam_type_check CHECK (((type >= 0) AND (type <= 5)))
);