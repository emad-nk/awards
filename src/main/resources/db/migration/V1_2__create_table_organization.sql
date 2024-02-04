create table if not exists organization
(
    id                  varchar(100)    not null constraint organization_pk primary key,
    name                text            not null
);
