-- schema user
desc user;

insert into user values(null, '피카츄', 'pikachu@gmail.com', '1234', '여', now());

-- select01
select no, name from user where email='' and password='';

-- select02
select * from user;

select no, name, email, gender from user where no=1;

-- update user set name='긴주이', email='kje_0727@naver.com', password='1234', gender='female' where no=2;