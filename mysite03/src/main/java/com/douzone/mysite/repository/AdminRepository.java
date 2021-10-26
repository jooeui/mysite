package com.douzone.mysite.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.vo.SiteVo;

@Repository
public class AdminRepository {
	@Autowired
	private SqlSession sqlSession;

	public SiteVo getSiteInfo() {
		return sqlSession.selectOne("admin.getSiteInfo");
	}

	public boolean updateSite(SiteVo siteVo) {
		int count = sqlSession.update("admin.updateSite", siteVo);
		return count == 1;
	}
	
	
}
