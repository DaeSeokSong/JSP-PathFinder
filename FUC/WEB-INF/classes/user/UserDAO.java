package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserDAO { //DAO 데이터베이스에서 데이터 빼오는 클래스
	
	private DataSource dataSource;
	
	public UserDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPassword) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USR WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("userPassword").equals(userPassword)) {
					return 1; //로그인 성공
				}
				return 2; //비밀번호 불일치
			}else {
				return 0; //해당 사용자가 없음
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}
	
	public int registerCheck(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USR WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next() || userID.equals("")) {
				return 0; //이미 존재하는 회원
			}else {
				return 1; //가입 가능
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}

	public int register(String userID, String userPassword, String userName, String userGender, String userEmail, String userProfile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO USR VALUES (?, ?, ?, ?, ?, ?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, userPassword);
			pstmt.setString(3, userName);
			pstmt.setString(4, userGender);
			pstmt.setString(5, userEmail);
			pstmt.setString(6, userProfile);
			
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}
	
	public UserDTO getUser(String userID) {
		UserDTO user = new UserDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USR WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user.setUserID(userID);
				user.setUserPassword(rs.getString("userPassword"));
				user.setUserName(rs.getString("userName"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setUserProfile(rs.getString("userProfile"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}
	
	public int update(String userID, String userPassword, String userName, String userGender, String userEmail) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE USR SET userPassword = ?, userName = ?, userGender = ?, userEmail = ? WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userPassword);
			pstmt.setString(2, userName);
			pstmt.setString(3, userGender);
			pstmt.setString(4, userEmail);
			pstmt.setString(5, userID);
			
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}
	
	public int profile(String userID, String userProfile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE USR SET userProfile = ? WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userProfile);
			pstmt.setString(2, userID);
			
			return pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1; //데이터베이스 오류
	}
	
	public String getProfile(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT userProfile FROM USR WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("userProfile").equals("") || rs.getString("userProfile") == null) {
					return "/FUC/images/icon.jpg";
				}
				return "/FUC/upload/" + rs.getString("userProfile");
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "/FUC/images/icon.jpg";
	}
	
	public ArrayList<UserDTO> getProfileList() {
		ArrayList<UserDTO> profileList = null;
		UserDAO userDAO = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USR";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			profileList = new ArrayList<UserDTO>();
			userDAO = new UserDAO();
			
			while(rs.next()) {
				UserDTO user = new UserDTO();
				
				user.setUserPassword(rs.getString("userPassword"));
				user.setUserName(rs.getString("userName"));
				user.setUserGender(rs.getString("userGender"));
				user.setUserEmail(rs.getString("userEmail"));
				
				if(rs.getString("userProfile").equals("")) {
					user.setUserProfile("/FUC/images/icon.jpg");
					user.setUserID(rs.getString("userID"));
					profileList.add(user);
				}
				else {
					user.setUserProfile(userDAO.getProfile(rs.getString("userID")));
					user.setUserID(rs.getString("userID"));
					profileList.add(user);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return profileList;
	}
	
	public String getUserName(String userID) {
		String userName = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM USR WHERE userID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString("userName").equals("")) {
					return "익명";
				} else {
					userName = rs.getString("userName");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return userName;
	}
	
}