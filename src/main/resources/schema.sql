drop table if exists cont_coverage CASCADE ;
drop table if exists contract CASCADE ;
drop table if exists coverage CASCADE ;
drop table if exists product CASCADE ;
drop table if exists test_domain CASCADE ;
drop sequence if exists cont_seq;
drop sequence if exists coverage_seq;
drop sequence if exists hibernate_sequence;
drop sequence if exists product_seq;
create sequence cont_seq start with 202200000 increment by 1;
create sequence coverage_seq start with 0 increment by 1;
create sequence hibernate_sequence start with 1 increment by 1;
create sequence product_seq start with 1000 increment by 1;

create table cont_coverage (
    cont_cvr_id bigint not null,
    cont_id bigint,
    cvr_id bigint,
    primary key (cont_cvr_id)
);

create table contract (
    cont_id bigint not null,
    cont_bg_date timestamp,
    cont_date bigint,
    cont_end_date timestamp,
    cont_price double,
    status varchar(255),
    prdt_id bigint,
    primary key (cont_id)
);

create table coverage (
   cvr_id bigint not null,
    base_price double,
    cvr_nm varchar(255),
    register_price double,
    prdt_id bigint,
    primary key (cvr_id)
);

create table product (
    prdt_id bigint not null,
    max_cont_date bigint,
    min_cont_date bigint,
    prdt_nm varchar(255),
    primary key (prdt_id)
);

create table test_domain (
   test_id bigint not null,
   name varchar(255),
   primary key (test_id)
);

alter table product
add constraint UK_a3t412ph0ibeka0je0nlmq8ne
unique (prdt_nm);

alter table cont_coverage
add constraint FKn6ltp6l87l6d7phet83jj0u9m
foreign key (cont_id)
references contract;

alter table cont_coverage
add constraint FKm0ii9lkmrdvax9ikwe6xdgvuy
foreign key (cvr_id)
references coverage;

alter table contract
add constraint FKavlvjl809wcqe0gfpaj0qylgx
foreign key (prdt_id)
references product;

alter table coverage
add constraint FK6pxa88g13itj33prpwpcs5hhq
foreign key (prdt_id)
references product;