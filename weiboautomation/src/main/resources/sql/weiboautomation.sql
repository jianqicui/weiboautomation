drop database weiboautomation;
create database if not exists weiboautomation default charset utf8 collate utf8_general_ci;

use weiboautomation;

create table pp_tid_type (
	id int not null auto_increment,
	pp_tid int not null,
	pp_tid_page_size int not null,
	pp_tid_page_index int not null,
	type_id int not null,
	primary key (id),
	foreign key (type_id) references type (id)
);

insert into pp_tid_type (pp_tid, type_id) values
(6, 1),
(97, 2),
(3, 3),
(139, 4),
(4, 5),
(7, 6),
(2, 7),
(1, 8),
(5, 9),
(88, 10),
(25, 11),
(8, 12),
(22, 13),
(23, 14),
(20, 15),
(19, 16),
(24, 17),
(17, 18),
(68, 19),
(43, 20);

create table operator_user_querying (
	id int not null auto_increment,
  	cookies text not null,
  	primary key (id)
);

create table user_collecting (
	id int not null auto_increment,
	sn varchar(20) not null,
	primary key (id)
);

insert into user_collecting (sn) values 
('1618051664'),
('1934183965'),
('2656274875'),
('2671109275'),
('2803301701');

create table user_collected (
	id int not null auto_increment,
	sn varchar(20) not null,
	primary key (id)
);

create table user_filtered (
	id int not null auto_increment,
	sn varchar(20) not null,
	primary key (id)
);

create table operator_user_following (
	id int not null auto_increment,
	code int not null,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);

create table operator_user_filling (
	id int not null auto_increment,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);

create table user_profile (
	id int not null auto_increment,
  	sn varchar(20) not null,
  	name varchar(60) not null,
  	gender varchar(10),
  	location varchar(20),
  	tags varchar(200),
  	primary key (id)
);

create table operator_user_transfering (
	id int not null auto_increment,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);

create table operator_message_adding_globally (
	id int not null auto_increment,
	sn varchar(20) not null,
  	cookies text not null,
  	text varchar(200) not null,
  	user_size int not null,
  	begin_date date not null,
  	end_date date not null,
  	hours varchar(100) not null,
  	primary key (id)
);

create table operator_message_adding_individually (
	id int not null auto_increment,
	sn varchar(20) not null,
  	cookies text not null,
  	text varchar(200) not null,
  	user_size int not null,
  	begin_date date not null,
  	end_date date not null,
  	hours varchar(100) not null,
  	user_base_table_name varchar(40) not null,
  	user_base_index int not null,
  	primary key (id)
);

create table operator_comment_adding_globally (
	id int not null auto_increment,
	sn varchar(20) not null,
  	cookies text not null,
  	text varchar(200) not null,
  	user_size int not null,
  	begin_date date not null,
  	end_date date not null,
  	hours varchar(100) not null,
  	primary key (id)
);

create table operator_comment_adding_individually (
	id int not null auto_increment,
	sn varchar(20) not null,
  	cookies text not null,
  	text varchar(200) not null,
  	user_size int not null,
  	begin_date date not null,
  	end_date date not null,
  	hours varchar(100) not null,
  	user_base_table_name varchar(40) not null,
  	user_base_index int not null,
  	primary key (id)
);
