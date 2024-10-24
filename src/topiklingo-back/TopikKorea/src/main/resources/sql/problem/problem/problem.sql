CREATE TABLE problem
(
    id         varchar(255) PRIMARY KEY NOT NULL,
    etype      smallint,
    ptype      smallint,
    example    text,
    problem    varchar(255),
    created_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    CONSTRAINT problem_etype_check CHECK (((etype >= 0) AND (etype <= 2))),
    CONSTRAINT problem_ptype_check CHECK (((ptype >= 0) AND (ptype <= 44)))
);
