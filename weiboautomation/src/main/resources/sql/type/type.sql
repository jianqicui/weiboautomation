use weiboautomation;

create table type (
	id int not null auto_increment,
	code int not null,
	name varchar(10) not null,
	primary key (id)
);

insert into type (code, name) values 
(1, '经典语录'),
(2, '影视世界'),
(3, '幽默搞笑'),
(4, '奇闻异事'),
(5, '星座物语'),
(6, '生活百科'),
(7, '游戏天下'),
(8, '汽车之家'),
(9, '美食工厂'),
(10, '亲子乐园'),
(11, '创意无限'),
(12, '时尚潮流'),
(13, '时尚家居'),
(14, '萌宠动物'),
(15, '心理测试'),
(16, '职场人生'),
(17, '风景旅行'),
(18, '小说故事'),
(19, '内涵漫画'),
(20, '移动终端');
