-- schema
desc board;

-- select
select * from board;
select * from user;

-- reg_date가 오늘 날짜면 시간, 과거이면 날짜로 출력
select if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y-%m-%d')) from board;

select b.no, b.title, b.hit, 
		if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d.')) as reg_date, 
        b.group_no, b.order_no, b.depth, u.name 
from board b, user u 
where b.user_no = u.no
order by b.group_no desc, b.order_no asc
limit 0, 10;

-- insert 
-- insert into board values(null, '', '테스트 하고 있습니다', default, now(), ifnull((select max(group_no) from board b), 0)+1, 0, 0, 1);


-- reply update 
update board set order_no=order_no-1 where group_no=1 and order_no >0;
update board set order_no=0 where no=2;
-- reply insert
insert into board values(null, '일단 workbench에서 해보자', 'MySQL Workbench에서 해보자고~!', default, now(), 1, 1, 2, 1);