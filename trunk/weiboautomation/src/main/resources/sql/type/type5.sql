use weiboautomation;

create table type5_blog (
	id int not null auto_increment,
	text varchar(288) not null,
	picture varchar(200),
	primary key (id)
);

create table type5_operator_blog_transfering (
	id int not null auto_increment,
  	cookies text not null,
  	blog_index int not null,
  	primary key (id)
);

create table type5_operator_blog_publishing (
	id int not null auto_increment,
  	cookies text not null,
  	primary key (id)
);

create table type5_user_filtered (
	id int not null auto_increment,
	sn varchar(20) not null,
	primary key (id)
);

create table type5_operator_user_following (
	id int not null auto_increment,
	code int not null,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);

create table type5_operator_blog_publishing_timingly (
	id int not null auto_increment,
	sn varchar(20) not null,
  	cookies text not null,
  	begin_date date not null,
  	end_date date not null,
  	hours varchar(100) not null,
  	primary key (id)
);
