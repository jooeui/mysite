package com.douzone.mysite.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.vo.BoardVo;

@Repository
public class BoardRepository {
	@Autowired
	private SqlSession sqlSession;
	
	public Long countAll(String searchType, String keyword) {
		Map<String, String> map = new HashMap<>();
		map.put("searchType", searchType);
		map.put("keyword", keyword);
		return sqlSession.selectOne("board.countAll", map);
	}
	
	public List<BoardVo> findPrintList(String searchType, String keyword, int listLimit, int limitCount) {
		Map<String, Object> map = new HashMap<>();
		map.put("searchType", searchType);
		map.put("keyword", keyword);
		map.put("listlimit", listLimit);
		map.put("limitcount", limitCount);
//		System.out.println("map = " + map);
//		System.out.println("keyword" + map.get("keyword").getClass());
		return sqlSession.selectList("board.findPrintList", map);
	}
	
	public BoardVo findByPost(Long no) {
		return sqlSession.selectOne("board.findByPost", no);
	}
	
	public boolean hitUpdate(Long no) {
		int count = sqlSession.update("board.hitUpdate", no);
		return count == 1;
	}

	public Long write(BoardVo boardVo) {
		sqlSession.insert("board.write", boardVo);
		Long postNo = boardVo.getNo();
		return postNo;
	}

	public BoardVo findByParentBoardInfo(Long no) {
		return sqlSession.selectOne("board.findByParentBoardInfo", no);
	}

	public boolean orderNoUpdate(BoardVo boardVo) {
		int count = sqlSession.update("board.orderNoUpdate", boardVo);
		return count == 1;
	}
	
	public BoardVo findByPostInfo(Long no, Long userNo) {
		Map<String, Long> map = new HashMap<>();
		map.put("no", no);
		map.put("userNo", userNo);
		return sqlSession.selectOne("board.findByEditPostInfo", map);
	}
	
	public boolean update(BoardVo boardVo) {
		int count = sqlSession.update("board.postUpdate", boardVo);
		return count == 1;
	}

//	public boolean delete(Long no, Long userNo, String password) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("no", no);
//		map.put("userNo", userNo);
//		map.put("password", password);
//		
//		int count = sqlSession.delete("board.deletePost", map);
//		return count == 1;
//			
//	}
	public boolean delete(Long no, Long userNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("no", no);
		map.put("userNo", userNo);
//		map.put("password", password);
		
		int count = sqlSession.delete("board.deletePost", map);
		return count == 1;
		
	}

//	public Long searchCount(String kwd) {
//		Long searchCount = sqlSession.selectOne("board.searchCount", kwd);
//		return searchCount;
//	}

//	public List<BoardVo> searchPrintList(long listLimit, Long limitCount, String kwd) {
//		List<BoardVo> result = new ArrayList<>();
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = getConnection();
//			
//			String sql = "select b.no, b.title, b.hit, " +
//						 "if(curdate() = date_format(reg_date, '%Y-%m-%d'), "
//						 	+ " date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d')), " + 
//						" b.group_no, b.order_no, b.depth, u.name, b.user_no " + 
//						" from board b, user u " + 
//						" where b.user_no = u.no " + 
//						"	and title like ?" +
//						"	and delete_flag = 'n' " + 
//						" order by b.group_no desc, b.order_no asc " + 
//						" limit ?, ?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, "%"+kwd+"%");
//			pstmt.setLong(2, listLimit);
//			pstmt.setLong(3, limitCount);
//			
//			rs = pstmt.executeQuery();
//			while(rs.next()) {
//				Long no = rs.getLong(1);
//				String title = rs.getString(2);
//				Long hit = rs.getLong(3);
//				String regDate = rs.getString(4);
//				Long groupNo = rs.getLong(5);
//				Long orderNo = rs.getLong(6);
//				Long depth = rs.getLong(7);
//				String writer = rs.getString(8);
//				Long userNo = rs.getLong(9);
//				
//				BoardVo vo = new BoardVo();
//				vo.setNo(no);
//				vo.setTitle(title);
//				vo.setHit(hit);
//				vo.setRegDate(regDate);
//				vo.setGroupNo(groupNo);
//				vo.setOrderNo(orderNo);
//				vo.setDepth(depth);
//				vo.setWriter(writer);
//				vo.setUserNo(userNo);
//				
//				result.add(vo);
//			}
//		} catch (SQLException e) {
//			System.out.println("error: " + e);
//		} finally {
//			try {
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
}
