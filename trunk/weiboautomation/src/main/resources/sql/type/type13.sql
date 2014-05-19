use weiboautomation;

create table type13_blog (
	id int not null auto_increment,
	text varchar(288) not null,
	picture varchar(200),
	primary key (id)
);

create table type13_user_transfering (
	id int not null auto_increment,
  	cookies text not null,
  	blog_index int not null,
  	primary key (id)
);

create table type13_user_publishing (
	id int not null auto_increment,
  	cookies text not null,
  	primary key (id)
);

create table type13_user_following (
	id int not null auto_increment,
	code int not null,
  	cookies text not null,
  	user_index int not null,
  	primary key (id)
);
