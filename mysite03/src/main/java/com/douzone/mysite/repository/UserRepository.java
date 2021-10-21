package com.douzone.mysite.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.mysite.exception.UserRepositoryException;
import com.douzone.mysite.vo.UserVo;

@Repository
public class UserRepository {
	@Autowired
	private DataSource dataSource;
	
	public boolean insert(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = dataSource.getConnection();
			
			// 3. SQL 준비
			String sql = "insert into user values(null, ?, ?, ?, ?, now())";
			pstmt = conn.prepareStatement(sql);
			
			// 4. 바인딩(binding)
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			
			// 5. SQL 실행
			int count = pstmt.executeUpdate();
			
			result = count == 1;
		} catch (SQLException e) {
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
	
	public UserVo findByEmailAndPassword(String email, String password) throws UserRepositoryException {
		UserVo vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select no, name from user " + 
						" where email=? " + 
						" 	and password=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			// 5. SQL 실행
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long no = rs.getLong(1);
				String name = rs.getString(2);
				
				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}
		} catch (SQLException e) {
			throw new UserRepositoryException(e.toString());
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
		
		return vo;
	}

	public UserVo findByNo(Long no) throws UserRepositoryException {
		UserVo vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = dataSource.getConnection();
			
			String sql = "select no, name, email, gender from user where no=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Long uNo = rs.getLong(1);
				String name = rs.getString(2);
				String email = rs.getString(3);
				String gender = rs.getString(4);
				
				vo = new UserVo();
				vo.setNo(uNo);
				vo.setName(name);
				vo.setEmail(email);
				vo.setGender(gender);
			}
			
			
		} catch (SQLException e) {
			throw new UserRepositoryException(e.toString());
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
		return vo;
	}

	public boolean update(UserVo vo) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = dataSource.getConnection();
			
			if("".equals(vo.getName())) {	
				// name이 빈값인 경우 - name을 update 하지 않음
				if("".equals(vo.getPassword())) {	
					// password가 빈값인 경우 - password를 update 하지 않음
					sql = "update user " + 
							" set gender=? " + 
							" where no=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, vo.getGender());
					pstmt.setLong(2, vo.getNo());
				} else {	
					// password에 값이 있는 경우
					sql = "update user " + 
							" set password=?, gender=? " + 
							" where no=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, vo.getPassword());
					pstmt.setString(2, vo.getGender());
					pstmt.setLong(3, vo.getNo());
				}
			} else { 	
				// name에 값이 있는 경우
				if("".equals(vo.getPassword())) {	
					// password가 빈값인 경우 - password를 update 하지 않음
					sql = "update user " + 
							" set name=?, gender=? " + 
							" where no=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, vo.getName());
					pstmt.setString(2, vo.getGender());
					pstmt.setLong(3, vo.getNo());
				} else {	
					// password에 값이 있는 경우
					sql = "update user " + 
							" set name=?, password=?, gender=? " + 
							" where no=?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setString(1, vo.getName());
					pstmt.setString(2, vo.getPassword());
					pstmt.setString(3, vo.getGender());
					pstmt.setLong(4, vo.getNo());
				}
			}
			
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
	
}
