package tip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class TipDAO {
	
	private DataSource dataSource;
	
	public TipDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int write(String userID, String tipLang, String tipTitle, String tipContent, String tipFile, String tipRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO TIP SELECT ?, IFNULL((SELECT MAX(tipID) + 1 FROM TIP), 1), ?, ?, now(), ?, 0, ?, ?, IFNULL((SELECT MAX(tipGroup) + 1 FROM TIP), 0), 0, 0, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, tipTitle);
			pstmt.setString(3, tipContent);
			pstmt.setString(4, tipLang);
			pstmt.setString(5, tipFile);
			pstmt.setString(6, tipRealFile);
			
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
	
	public TipDTO getTip(String tipID) {
		TipDTO tip = new TipDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM TIP WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, tipID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				tip.setUserID(rs.getString("userID"));
				tip.setTipID(rs.getInt("tipID"));
				tip.setTipTitle(rs.getString("tipTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				tip.setTipContent(rs.getString("tipContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				tip.setTipDate(rs.getString("tipDate").substring(0, 11));
				tip.setTipLang(rs.getString("tipLang"));
				tip.setTipHit(rs.getInt("tipHit"));
				tip.setTipFile(rs.getString("tipFile"));
				tip.setTipRealFile(rs.getString("tipRealFile"));
				tip.setTipGroup(rs.getInt("tipGroup"));
				tip.setTipSequence(rs.getInt("tipSequence"));
				tip.setTipLevel(rs.getInt("tipLevel"));
				tip.setTipAvailable(rs.getInt("tipAvailable"));
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
		return tip;
	}
	
	public ArrayList<TipDTO> getList(String pageNumber) {
		ArrayList<TipDTO> tipList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM TIP WHERE tipGroup > (SELECT MAX(tipGroup) FROM TIP) - ? AND tipGroup <= (SELECT MAX(tipGroup) FROM TIP) - ? ORDER BY tipGroup DESC, tipSequence ASC";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			pstmt.setInt(2, (Integer.parseInt(pageNumber) - 1) * 10);
			rs = pstmt.executeQuery();
			tipList = new ArrayList<TipDTO>();
			while(rs.next()) {
				TipDTO tip = new TipDTO();
				tip.setUserID(rs.getString("userID"));
				tip.setTipID(rs.getInt("tipID"));
				tip.setTipTitle(rs.getString("tipTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				tip.setTipContent(rs.getString("tipContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				tip.setTipDate(rs.getString("tipDate").substring(0, 11));
				tip.setTipLang(rs.getString("tipLang"));
				tip.setTipHit(rs.getInt("tipHit"));
				tip.setTipFile(rs.getString("tipFile"));
				tip.setTipRealFile(rs.getString("tipRealFile"));
				tip.setTipGroup(rs.getInt("tipGroup"));
				tip.setTipSequence(rs.getInt("tipSequence"));
				tip.setTipLevel(rs.getInt("tipLevel"));
				tip.setTipAvailable(rs.getInt("tipAvailable"));
				tipList.add(tip);
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
		return tipList;
	}
	
	public int hit(String tipID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE TIP SET tipHit = tipHit + 1 WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, tipID);
			
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
	
	public String getFile(String tipID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT tipFile FROM TIP WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, tipID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("tipFile");
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
	
	public String getRealFile(String tipID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT tipRealFile FROM TIP WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, tipID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("tipRealFile");
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
	
	public int update(String tipID, String tipLang, String tipTitle, String tipContent, String tipFile, String tipRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE TIP SET tipTitle = ?, tipContent = ?, tipLang = ?, tipFile = ?, tipRealFile = ? WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, tipTitle);
			pstmt.setString(2, tipContent);
			pstmt.setString(3, tipLang);
			pstmt.setString(4, tipFile);
			pstmt.setString(5, tipRealFile);
			pstmt.setInt(6, Integer.parseInt(tipID));
			
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
	
	public int delete(String tipID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE TIP SET tipAvailable = 0 WHERE tipID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(tipID));
			
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
	
	public int reply(String userID, String tipLang, String tipTitle, String tipContent, String tipFile, String tipRealFile, TipDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO TIP SELECT ?, IFNULL((SELECT MAX(tipID) + 1 FROM TIP), 1), ?, ?, now(), ?, 0, ?, ?, ?, ?, ?, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, tipTitle);
			pstmt.setString(3, tipContent);
			pstmt.setString(4, tipLang);
			pstmt.setString(5, tipFile);
			pstmt.setString(6, tipRealFile);
			pstmt.setInt(7, parent.getTipGroup());
			pstmt.setInt(8, parent.getTipSequence() + 1);
			pstmt.setInt(9, parent.getTipLevel() + 1);
			
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
	
	public int replyUpdate(TipDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE TIP SET tipSequence = tipSequence + 1 WHERE tipGroup = ? AND tipSequence > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getTipGroup());
			pstmt.setInt(2, parent.getTipSequence());
			
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
		String SQL = "SELECT * FROM TIP WHERE tipGroup >= ?";
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
		String SQL = "SELECT COUNT(tipGroup) FROM TIP WHERE tipGroup > ?";
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
