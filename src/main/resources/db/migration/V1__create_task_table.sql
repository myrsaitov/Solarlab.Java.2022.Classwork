CREATE SEQUENCE hibernate_sequence
    INCREMENT 1
    START 2
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
;

CREATE TABLE task
(
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    started_at timestamp without time zone NOT NULL,
    status integer NOT NULL,
    CONSTRAINT task_pkey PRIMARY KEY (id)
)
;