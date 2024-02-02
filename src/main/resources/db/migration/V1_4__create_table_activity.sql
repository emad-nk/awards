create table if not exists activity
(
    id                  varchar(100)    not null constraint activity_pk primary key,
    occurred_at         timestamp       not null,
    event               text            not null
);
