-- schema user
desc user;

insert into user values(null, '피카츄', 'pikachu@gmail.com', '1234', '여', now());

-- select01
select no, name from user where email='' and password='';

-- select02
select * from user;