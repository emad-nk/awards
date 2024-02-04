create table if not exists employee
(
    id                  varchar(100)    not null constraint employee_pk primary key,
    first_name          text            not null,
    last_name           text            not null,
    dundie_awards       integer         not null default 0,
    organization_id     varchar(100),
    constraint employee_fkey
        foreign key (organization_id)
        references organization(id) on delete cascade
);
