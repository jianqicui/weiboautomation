use weiboautomation;

create table type14_blog (
	id int not null auto_increment,
	text varchar(288) not null,
	picture varchar(200),
	primary key (id)
);

create table type14_operator_blog_transfering (
	id int not null auto_increment,
  	cookies text not null,
  	blog_index int not null,
  	primary key (id)
);

create table type14_operator_blog_publishing (
	id int not null auto_increment,
  	cookies text not null,
  	primary key (id)
);

create table type14_user_filtered (
	id int not null auto_increment,
	sn varchar(20) not null,
	primary key (id)
);

create table type14_operator_user_following (
	id int not null auto_increment,
	code int not null,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);
