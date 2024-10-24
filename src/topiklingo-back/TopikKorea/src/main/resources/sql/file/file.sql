CREATE TABLE file
(
    id         uuid PRIMARY KEY NOT NULL,
    file_url   varchar(255),
    object_id  varchar(255),
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone
);