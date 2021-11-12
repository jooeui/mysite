package com.douzone.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.exception.GuestbookRepositoryException;
import com.douzone.mysite.vo.GuestbookVo;

@Repository
public class GuestbookRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public GuestbookVo findByPassword(Long no, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("password", password);
		
		return sqlSession.selectOne("guestbook.findByPassword", map);
	}
	
	public List<GuestbookVo> findAll() throws GuestbookRepositoryException{
		return sqlSession.selectList("guestbook.findAll");
	}
	
	public List<GuestbookVo> findAllLimit(Long no) {
		return sqlSession.selectList("guestbook.findAllLimit", no);
	}
	
	public boolean insert(GuestbookVo vo) {
		int count = sqlSession.insert("guestbook.insert", vo);
		System.out.println(vo);
		return count == 1;
	}
	
	public boolean delete(Long no, String password) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("password", password);
		
		int count = sqlSession.insert("guestbook.delete", map);
		return count == 1;
	}
}
