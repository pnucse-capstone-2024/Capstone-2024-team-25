CREATE TABLE write_problem
(
    id         bigint PRIMARY KEY NOT NULL,
    wtype      smallint,
    problem    varchar(255),
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    CONSTRAINT write_problem_wtype_check CHECK (((etype >= 0) AND (etype <= 2)))
);
