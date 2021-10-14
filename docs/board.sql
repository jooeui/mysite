-- schema
desc board;

select * from board;

select if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y-%m-%d')) from board;

select b.no, b.title, b.hit, 
		if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d.')) as reg_date, 
        b.group_no, b.order_no, b.depth, u.name 
from board b, user u 
where b.user_no = u.no
order by b.group_no desc, b.order_no asc;

select no, title, content, group_no, order_no, depth, user_no
from board b;

insert into board 
values(null, '', '테스트 하고 있습니다', default, now(), ifnull((select max(group_no) from board b), 0)+1, 0, 0, 1);
update board set title='1', content='테스트' where no=2;