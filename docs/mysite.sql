-- schema user
desc user;

insert into user values(null, '피카츄', 'pikachu@gmail.com', '1234', '여', now());

-- select01
select no, name from user where email='' and password='';

-- select02
select * from user order by no desc;

select no, name, email, gender from user where no=1;

-- update user set name='긴주이', email='kje_0727@naver.com', password='1234', gender='female' where no=2;

alter table user add column role enum('USER', 'ADMIN') not null default 'USER';
insert into user values(null, '관리자', 'admin@mysite.com', 'admin', 'female', now(), 'ADMIN');

desc site;
select * from site;
insert into site values(null, 'MySite', '안녕하세요. 김주의의 mysite에 오신 것을 환영합니다.', 'assets/images/pikachu',
						'이 사이트는 웹 프로그램밍 실습과제 예제 사이트입니다.\n메뉴는 사이트 소개, 방명록, 게시판이 있구요.\nJava 수업 + 데이터베이스 수업 + 웹프로그래밍 수업 배운 거 있는거 없는 거 다 합쳐서 만들어 놓은 사이트 입니다.');
update site set profile='/assets/images/pikachu.png' where no=1;
delete from user where no=14;