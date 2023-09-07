create table roles
(
    id   bigserial primary key,
    name varchar(20)
);
alter table roles
    owner to postgres;

insert into roles(name) values ('ROLE_USER');