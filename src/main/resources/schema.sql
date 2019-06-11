create schema if not exists dbo;

create table dbo.t_crawl(
id_crawl identity(1,1) not null,
domain_url varchar(1000) not null,
crawl_date_time datetime not null);

create table dbo.t_crawl_content(
id_crawl_content identity(1,1) not null,
content_type varchar(150) not null,
crawl_id int not null,
content blob not null,
foreign key (crawl_id) references dbo.t_crawl(id_crawl));