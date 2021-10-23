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
	
	public Long countAll() {
		return sqlSession.selectOne("board.countAll");
	}
	
	public List<BoardVo> findPrintList(int listLimit, int limitCount) {
		Map<String, Integer> map = new HashMap<>();
		map.put("listlimit", listLimit);
		map.put("limitcount", limitCount);
		
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

//	public boolean update(BoardVo vo) {
//		boolean result = false;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			conn = getConnection();
//			String sql = " update board set title=?, content=? where no=? and user_no=?";
//			pstmt = conn.prepareStatement(sql);
//			
//			pstmt.setString(1, vo.getTitle());
//			pstmt.setString(2, vo.getContent());
//			pstmt.setLong(3, vo.getNo());
//			pstmt.setLong(4, vo.getUserNo());
//			
//			int count = pstmt.executeUpdate();
//			
//			result = count == 1;
//		} catch(SQLException e) {
//			System.out.println("error: " + e);
//		} finally {
//			// clean up
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
//
	public BoardVo findByParentBoardInfo(Long no) {
		return sqlSession.selectOne("board.findByParentBoardInfo", no);
	}

	public boolean orderNoUpdate(BoardVo boardVo) {
		int count = sqlSession.update("board.orderNoUpdate", boardVo);
		return count == 1;
	}


//	public boolean delete(BoardVo vo, String pw) {
//		boolean result = false;
//		Connection conn = null;
//		String sql = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			conn = getConnection();
//			
//			sql = "update board set delete_flag='Y' " + 
//				 " where no=? and user_no=? " + 
//					" and if((select count(*) from user where no=? and password=?) = 1, true, false) ";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setLong(1, vo.getNo());
//			pstmt.setLong(2, vo.getUserNo());
//			pstmt.setLong(3, vo.getUserNo());
//			pstmt.setString(4, pw);
//			
//			int count = pstmt.executeUpdate();
//			
//			result = count == 1;
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
//
//	public Long searchCount(String kwd) {
//		Long searchCount = 0L;
//		
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			conn = getConnection();
//			
//			String sql = "select count(*) from board" + 
//						" where title like ?" +
//						"	and delete_flag = 'n' ";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, "%"+kwd+"%");
//			
//			rs = pstmt.executeQuery();
//			while(rs.next()) {
//				searchCount = rs.getLong(1);
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
//
//		return searchCount;
//	}
//	
//
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
