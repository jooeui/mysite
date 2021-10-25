-- schema
desc board;

-- select
select * from board order by no desc;
select * from user;

-- reg_date가 오늘 날짜면 시간, 과거이면 날짜로 출력
select if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y-%m-%d')) from board;

select b.no, b.title, b.hit, 
		if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d.')) as reg_date, 
        b.group_no, b.order_no, b.depth, u.name, b.delete_flag
from board b, user u 
where b.user_no = u.no
order by b.group_no desc, b.order_no asc;

-- insert 
-- insert into board values(null, '', '테스트 하고 있습니다', default, now(), ifnull((select max(group_no) from board b), 0)+1, 0, 0, 1);

-- reply update 
update board set order_no=order_no-1 where group_no=1 and order_no >0;
update board set order_no=0 where no=2;

-- reply insert
insert into board values(null, '일단 workbench에서 해보자', 'MySQL Workbench에서 해보자고~!', default, now(), 1, 1, 2, 1);

-- 글 삭제
update board set order_no=1 and depth=1 where no=39;
-- update board set delete_flag='Y' where no=23 and user_no=2 and if((select count(*) from user where no=1 and password='12345') = 1, true, false);
-- select no, group_no, delete_flag
-- from board b,
-- 	 (select no, group_no, delete_flag
--       from board
-- 	  where delete_flag = 'Y' 
-- 		and order_no);

select * from board;

select group_no, count(*) as count
from board
where delete_flag = 'Y'
group by group_no;

select b.no
from board b,
	(select group_no, count(*) as count
		from board
		group by group_no) b2
where b.group_no = b2.group_no
	and order_no = 0
	and delete_flag = 'Y'
    and b2.count = 1;
    
select b.no
from board b,
	(select group_no, count(*) as count
	from board
	group by group_no) b2,
    (select group_no, count(*) as count
	from board
	where delete_flag='Y'
	group by group_no) b3
where b.group_no = b2.group_no
	and b2.group_no = b3.group_no
	and b2.count = b3.count;
    
(select group_no, count(*) as count from board group by group_no having count > 1);
select no
from board b, 
	(select group_no, max(depth) as max from board group by group_no) b2
where b.group_no = b2.group_no
	and delete_flag = 'Y'
	and order_no > 1
    and b.depth = b2.max;

select b.no
from board b,
	(select group_no, count(*) as count
	from board
	group by group_no) b2,
    (select group_no, count(*) as count
	from board
	where delete_flag='Y'
	group by group_no) b3
where b.group_no = b2.group_no
	and b2.group_no = b3.group_no
	and b2.count = b3.count
;

select b.no, b.title, b.hit, 
		if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d.')) as reg_date, 
        b.group_no, b.order_no, b.depth, u.name, b.delete_flag
from board b, user u
where b.user_no = u.no
	and b.no not in (select b1.no
					from board b1,
						(select group_no, count(*) as count
						from board
						group by group_no) b2,
						(select group_no, count(*) as count
						from board
						where delete_flag='Y'
						group by group_no) b3
					where b1.group_no = b2.group_no
						and b2.group_no = b3.group_no
						and b2.count = b3.count)
	and name like '%주의%'
order by b.group_no desc, b.order_no asc;
select count(*) from board;
select count(*) from board
where no not in (select b.no
				 from board b,
					  (select group_no, count(*) as count
					   from board
							group by group_no) b2
						where b.group_no = b2.group_no
							and order_no = 0
							and delete_flag = 'Y'
							and b2.count = 1)
		and title like '%%';

-- select b.no, b.title, b.hit, 
-- 		if(curdate()=date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d.')) as reg_date, 
--         b.group_no, b.order_no, b.depth, u.name, b.delete_flag 
-- from board b, user u 
-- where b.user_no = u.no
-- 	and (delete_flag = 'N' 
-- 			or (if((select count(*) from board group by group_no ), true, false)
-- order by b.group_no desc, b.order_no asc
-- limit 0, 5;

-- 검색
select b.no, b.title, b.hit, 
		if(curdate() = date_format(reg_date, '%Y-%m-%d'), date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d')),
        b.group_no, b.order_no, b.depth, u.name, b.delete_flag
from board b, user u
where b.user_no = u.no
	and title like '%%'
    and delete_flag = 'N'
order by b.group_no desc, b.order_no asc;