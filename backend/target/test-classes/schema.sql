CREATE TABLE "user"
(
    user_id              bigserial primary key,
    email                varchar(100) unique,
    password             varchar(100),
    phone_number         varchar(20) unique,
    first_name           varchar(20),
    last_name            varchar(20),
    drive_licence_number varchar(20) unique,
    register_date        timestamp
);

CREATE TABLE car
(
    car_id                  bigserial primary key,
    car_brand               varchar(20),
    car_model               varchar(20),
    car_registration_number varchar(20) unique,
    car_year                    int,
    car_status              varchar(10)
);

CREATE TABLE tariff
(
    tariff_id          bigserial primary key,
    name               varchar(20) unique,
    price   bigint,
    tariff_type varchar(10)
);

CREATE TABLE drive
(
    drive_id       bigserial primary key,
    user_id        bigserial,
    tariff_id      bigserial,
    car_id         bigserial,
    start_rental   timestamp,
    end_rental     timestamp,
    total_distance bigint,
    total_amount   bigint,
    drive_status   varchar(15),
    foreign key (user_id) references "user" (user_id),
    foreign key (car_id) references car (car_id),
    foreign key (tariff_id) references tariff (tariff_id)
);

CREATE TABLE payment
(
    payment_id     bigserial primary key,
    drive_id       bigserial,
    payment_amount bigint,
    payment_method varchar(10),
    payment_date   timestamp,
    payment_status varchar(10),
    foreign key (drive_id) references drive (drive_id)

);

create table role
(
    role_id  bigserial primary key,
    rolename varchar(50)
);

create table user_role
(
    user_id bigserial,
    role_id bigserial,
    primary key (user_id, role_id),
    foreign key (user_id) references "user" (user_id),
    foreign key (role_id) references role (role_id)
);
-- insert into role (rolename)
-- values ('ROLE_ADMIN');
-- insert into role (rolename)
-- values ('ROLE_USER');
--
-- drop table "user" cascade ;
-- drop table car cascade;
-- drop table tariff cascade;
-- drop table drive cascade;
-- drop table payment cascade;
-- drop table role cascade;
-- drop table user_role cascade;
--
-- truncate table "user" cascade ;
-- truncate table car cascade ;
-- truncate table tariff cascade ;
-- truncate table drive cascade ;
-- truncate table user_role cascade;


