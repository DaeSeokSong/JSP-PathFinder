package community;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class CommunityDAO {
	
	private DataSource dataSource;

	public CommunityDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int write(String userID, String comLang, String comTitle, String comContent, String comFile, String comRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO COM SELECT ?, IFNULL((SELECT MAX(comID) + 1 FROM COM), 1), ?, ?, now(), ?, 0, ?, ?, IFNULL((SELECT MAX(comGroup) + 1 FROM COM), 0), 0, 0, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, comTitle);
			pstmt.setString(3, comContent);
			pstmt.setString(4, comLang);
			pstmt.setString(5, comFile);
			pstmt.setString(6, comRealFile);
			
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
	
	public CommunityDTO getCom(String comID) {
		CommunityDTO com = new CommunityDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM COM WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, comID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				com.setUserID(rs.getString("userID"));
				com.setComID(rs.getInt("comID"));
				com.setComTitle(rs.getString("comTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				com.setComContent(rs.getString("comContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				com.setComDate(rs.getString("comDate").substring(0, 11));
				com.setComLang(rs.getString("comLang"));
				com.setComHit(rs.getInt("comHit"));
				com.setComFile(rs.getString("comFile"));
				com.setComRealFile(rs.getString("comRealFile"));
				com.setComGroup(rs.getInt("comGroup"));
				com.setComSequence(rs.getInt("comSequence"));
				com.setComLevel(rs.getInt("comLevel"));
				com.setComAvailable(rs.getInt("comAvailable"));
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
		return com;
	}
	
	public ArrayList<CommunityDTO> getList(String pageNumber) {
		ArrayList<CommunityDTO> comList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM COM WHERE comGroup > (SELECT MAX(comGroup) FROM COM) - ? AND comGroup <= (SELECT MAX(comGroup) FROM COM) - ? ORDER BY comGroup DESC, comSequence ASC";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			pstmt.setInt(2, (Integer.parseInt(pageNumber) - 1) * 10);
			rs = pstmt.executeQuery();
			comList = new ArrayList<CommunityDTO>();
			while(rs.next()) {
				CommunityDTO com = new CommunityDTO();
				com.setUserID(rs.getString("userID"));
				com.setComID(rs.getInt("comID"));
				com.setComTitle(rs.getString("comTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				com.setComContent(rs.getString("comContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				com.setComDate(rs.getString("comDate").substring(0, 11));
				com.setComLang(rs.getString("comLang"));
				com.setComHit(rs.getInt("comHit"));
				com.setComFile(rs.getString("comFile"));
				com.setComRealFile(rs.getString("comRealFile"));
				com.setComGroup(rs.getInt("comGroup"));
				com.setComSequence(rs.getInt("comSequence"));
				com.setComLevel(rs.getInt("comLevel"));
				com.setComAvailable(rs.getInt("comAvailable"));
				comList.add(com);
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
		return comList;
	}
	
	public int hit(String comID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE COM SET comHit = comHit + 1 WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, comID);
			
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
	
	public String getFile(String comID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT comFile FROM COM WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, comID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("comFile");
			}
			return "";
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
		return "";
	}
	
	public String getRealFile(String comID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT comRealFile FROM COM WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, comID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("comRealFile");
			}
			return "";
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
		return "";
	}
	
	public int update(String comID, String comLang, String comTitle, String comContent, String comFile, String comRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE COM SET comTitle = ?, comContent = ?, comLang = ?, comFile = ?, comRealFile = ? WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, comTitle);
			pstmt.setString(2, comContent);
			pstmt.setString(3, comLang);
			pstmt.setString(4, comFile);
			pstmt.setString(5, comRealFile);
			pstmt.setInt(6, Integer.parseInt(comID));
			
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
	
	public int delete(String comID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE COM SET comAvailable = 0 WHERE comID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(comID));
			
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
	
	public int reply(String userID, String comLang, String comTitle, String comContent, String comFile, String comRealFile, CommunityDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO COM SELECT ?, IFNULL((SELECT MAX(comID) + 1 FROM COM), 1), ?, ?, now(), ?, 0, ?, ?, ?, ?, ?, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, comTitle);
			pstmt.setString(3, comContent);
			pstmt.setString(4, comLang);
			pstmt.setString(5, comFile);
			pstmt.setString(6, comRealFile);
			pstmt.setInt(7, parent.getComGroup());
			pstmt.setInt(8, parent.getComSequence() + 1);
			pstmt.setInt(9, parent.getComLevel() + 1);
			
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
	
	public int replyUpdate(CommunityDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE COM SET comSequence = comSequence + 1 WHERE comGroup = ? AND comSequence > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getComGroup());
			pstmt.setInt(2, parent.getComSequence());
			
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
	
	public boolean nextPage(String pageNumber) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM COM WHERE comGroup >= ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return true;
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
		return false;
	}
	
	public int targetPage(String pageNumber) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT COUNT(comGroup) FROM COM WHERE comGroup > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, (Integer.parseInt(pageNumber) - 1) * 10);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt(1) / 10;
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
		return 0;
	}
}
