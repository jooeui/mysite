package com.douzone.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.douzone.mysite.vo.BoardVo;


public class BoardDao {
	
	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			// 1. JDBC Driver 로딩
			// ClassNotFoundException은 던지면 받는 쪽에서 이상하기 때문에 여기서 처리
			Class.forName("org.mariadb.jdbc.Driver");

			// 2. 연결하기
			String url = "jdbc:mysql://127.0.0.1:3306/webdb?charset=utf8?";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 " + e);
		}

		return conn;
	}
	
	public Long countAll() {
		Long count = 0L;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select count(*) from board " + 
					 "	  where no not in (select b.no " + 
					 "					   from board b, " +
					 "							(select group_no, count(*) as count " + 
					 "							from board " + 
					 "							group by group_no) b2, " + 
					 "							(select group_no, count(*) as count " + 
					 "							from board " + 
					 "							where delete_flag='Y' " + 
					 "							group by group_no) b3 " + 
					 "						where b.group_no = b2.group_no " + 
					 "							and b2.group_no = b3.group_no " + 
					 "							and b2.count = b3.count) ";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				count = rs.getLong(1);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return count;
	}

	public boolean hitUpdate(Long no) {
		boolean result = false;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String hitUpdate = "update board set hit=hit+1 where no=? ";
			pstmt = conn.prepareStatement(hitUpdate);
			pstmt.setLong(1, no);
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Long write(BoardVo vo) {
		Long no = 0L;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "insert into board " + 
						" values(null, ?, ?, default, now(), ifnull((select max(group_no) from board b), 0)+1, 0, 0, ?, default) ";
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getUserNo());
			
			pstmt.executeUpdate();
			
			rs = pstmt.getGeneratedKeys();
			while(rs.next()) {
				no = rs.getLong(1);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return no;
	}
	
	public BoardVo findByPost(Long no) {
		BoardVo boardVo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select no, title, content, user_no " + 
						" from board where no=? and delete_flag='N'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long pNo = rs.getLong(1);
				String title = rs.getString(2);
				String content = rs.getString(3);
				Long userNo = rs.getLong(4);
				
				boardVo = new BoardVo();
				boardVo.setNo(pNo);
				boardVo.setTitle(title);
				boardVo.setContent(content);
				boardVo.setUserNo(userNo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return boardVo;
	}
	
	public boolean update(BoardVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			String sql = " update board set title=?, content=? where no=? and user_no=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setLong(3, vo.getNo());
			pstmt.setLong(4, vo.getUserNo());
			
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch(SQLException e) {
			System.out.println("error: " + e);
		} finally {
			// clean up
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public BoardVo findByParentBoardInfo(Long no) {
		BoardVo boardVo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select no, group_no, order_no, depth" + 
						" from board where no=? and delete_flag='N'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long pNo = rs.getLong(1);
				Long groupNo = rs.getLong(2);
				Long orderNo = rs.getLong(3);
				Long depth = rs.getLong(4);
				
				boardVo = new BoardVo();
				boardVo.setNo(pNo);
				boardVo.setGroupNo(groupNo);
				boardVo.setOrderNo(orderNo);
				boardVo.setDepth(depth);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return boardVo;
	}

	public boolean orderNoUpdate(BoardVo boardVo) {
		boolean result = false;
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			sql = "update board set order_no=order_no+1 where group_no=? and order_no>?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, boardVo.getGroupNo());
			pstmt.setLong(2, boardVo.getOrderNo());
			
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public boolean reply(BoardVo boardVo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			String sql = "insert into board values(null, ?, ?, default, now(), ?, ?, ?, ?, default) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setLong(3, boardVo.getGroupNo());
			pstmt.setLong(4, boardVo.getOrderNo()+1);
			pstmt.setLong(5, boardVo.getDepth()+1);
			pstmt.setLong(6, boardVo.getUserNo());
			
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<BoardVo> findPrintList(long listLimit, long limitCount) {
		List<BoardVo> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select b.no, b.title, b.hit, " +
						 "if(curdate() = date_format(reg_date, '%Y-%m-%d'), " + 
						 " date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d')), " + 
						 " b.group_no, b.order_no, b.depth, u.name, b.user_no, b.delete_flag " + 
						 " from board b, user u " + 
						 " where b.user_no = u.no " + 
						 "	  and b.no not in (select b.no " + 
						 "					   from board b, " +
						 "							(select group_no, count(*) as count " + 
						 "							from board " + 
						 "							group by group_no) b2, " + 
						 "							(select group_no, count(*) as count " + 
						 "							from board " + 
						 "							where delete_flag='Y' " + 
						 "							group by group_no) b3 " + 
						 "						where b.group_no = b2.group_no " + 
						 "							and b2.group_no = b3.group_no " + 
						 "							and b2.count = b3.count) " +
						 " order by b.group_no desc, b.order_no asc " + 
						 " limit ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, listLimit);
			pstmt.setLong(2, limitCount);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				Long hit = rs.getLong(3);
				String regDate = rs.getString(4);
				Long groupNo = rs.getLong(5);
				Long orderNo = rs.getLong(6);
				Long depth = rs.getLong(7);
				String writer = rs.getString(8);
				Long userNo = rs.getLong(9);
				String deleteFlag = rs.getString(10);
				
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setWriter(writer);
				vo.setUserNo(userNo);
				vo.setDeleteFlag(deleteFlag);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public boolean delete(BoardVo vo, String pw) {
		boolean result = false;
		Connection conn = null;
		String sql = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = getConnection();
			
			sql = "update board set delete_flag='Y' " + 
				 " where no=? and user_no=? " + 
					" and if((select count(*) from user where no=? and password=?) = 1, true, false) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, vo.getNo());
			pstmt.setLong(2, vo.getUserNo());
			pstmt.setLong(3, vo.getUserNo());
			pstmt.setString(4, pw);
			
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Long searchCount(String kwd) {
		Long searchCount = 0L;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select count(*) from board" + 
						" where title like ?" +
						"	and delete_flag = 'n' ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+kwd+"%");
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				searchCount = rs.getLong(1);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return searchCount;
	}
	

	public List<BoardVo> searchPrintList(long listLimit, Long limitCount, String kwd) {
		List<BoardVo> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			String sql = "select b.no, b.title, b.hit, " +
						 "if(curdate() = date_format(reg_date, '%Y-%m-%d'), "
						 	+ " date_format(reg_date, '%H:%i'), date_format(reg_date, '%Y.%m.%d')), " + 
						" b.group_no, b.order_no, b.depth, u.name, b.user_no " + 
						" from board b, user u " + 
						" where b.user_no = u.no " + 
						"	and title like ?" +
						"	and delete_flag = 'n' " + 
						" order by b.group_no desc, b.order_no asc " + 
						" limit ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+kwd+"%");
			pstmt.setLong(2, listLimit);
			pstmt.setLong(3, limitCount);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				Long hit = rs.getLong(3);
				String regDate = rs.getString(4);
				Long groupNo = rs.getLong(5);
				Long orderNo = rs.getLong(6);
				Long depth = rs.getLong(7);
				String writer = rs.getString(8);
				Long userNo = rs.getLong(9);
				
				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setRegDate(regDate);
				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
				vo.setWriter(writer);
				vo.setUserNo(userNo);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error: " + e);
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
