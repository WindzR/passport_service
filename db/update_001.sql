create table passportDB (
    id serial primary key not null,
    series varchar(200),
    expired_date timestamp
);

insert into passportDB (series, expired_date) values ('AA0001RU', '2023-06-01 00:00:00');
insert into passportDB (series, expired_date) values ('AA0002RU', '2023-06-12 00:00:00');
insert into passportDB (series, expired_date) values ('AB1234RU', '2022-06-01 00:00:00');
insert into passportDB (series, expired_date) values ('AX4321RU', '2022-09-01 00:00:00');