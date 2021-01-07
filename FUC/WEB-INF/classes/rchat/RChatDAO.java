package rchat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class RChatDAO { //데이터 베이스 접근

	private DataSource dataSource;
	
	public RChatDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<RChatDTO> getChatListByID(String fromID, String toID, String chatID) {
		ArrayList<RChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT * FROM RCHAT WHERE ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > ? ORDER BY chatTime";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, Integer.parseInt(chatID));
			rs = pstmt.executeQuery();
			chatList = new ArrayList<RChatDTO>();
			
			while(rs.next()) {
				RChatDTO chat = new RChatDTO();
				
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "오전";
				
				if(chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " " + rs.getString("chatTime").substring(14, 16));
				chatList.add(chat);
			}
		} catch(Exception e) {
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
		return chatList; //리스트 반환
	}
	
	public ArrayList<RChatDTO> getChatListByRecent(String fromID, String toID, int number) {
		ArrayList<RChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT * FROM RCHAT WHERE ((fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) AND chatID > (SELECT MAX(chatID) - ? FROM RCHAT WHERE (fromID = ? AND toID = ?) OR (fromID = ? AND toID = ?)) ORDER BY chatTime";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, toID);
			pstmt.setString(4, fromID);
			pstmt.setInt(5, number);
			pstmt.setString(6, fromID);
			pstmt.setString(7, toID);
			pstmt.setString(8, toID);
			pstmt.setString(9, fromID);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<RChatDTO>();
			
			while(rs.next()) {
				RChatDTO chat = new RChatDTO();
				
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "오전";
				
				if(chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " " + rs.getString("chatTime").substring(14, 16));
				chatList.add(chat);
			}
		} catch(Exception e) {
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
		return chatList; //리스트 반환
	}
	
	public int submit(String fromID, String toID, String chatContent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "INSERT INTO RCHAT VALUES (NULL, ?, ?, ?, NOW(), 0)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			pstmt.setString(3, chatContent);
			
			return pstmt.executeUpdate();
		} catch(Exception e) {
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
	
	public int readChat(String fromID, String toID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "UPDATE RCHAT SET chatRead = 1 WHERE (fromID = ? AND toID = ?)";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, toID);
			pstmt.setString(2, fromID);
			
			return pstmt.executeUpdate();
		} catch(Exception e) {
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
	
	public int getAllUnreadChat(String userID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT COUNT(chatID) FROM RCHAT WHERE toID = ? AND chatRead = 0";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("COUNT(chatID)");
			}
			return 0; //받은 메세지 없음
		} catch(Exception e) {
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
	
	public ArrayList<RChatDTO> getBox(String userID) {
		ArrayList<RChatDTO> chatList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT * FROM RCHAT WHERE chatID IN (SELECT MAX(chatID) FROM RCHAT WHERE toID = ? OR fromID = ? GROUP BY fromID, toID)";
		
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, userID);
			pstmt.setString(2, userID);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<RChatDTO>();
			
			while(rs.next()) {
				RChatDTO chat = new RChatDTO();
				
				chat.setChatID(rs.getInt("chatID"));
				chat.setFromID(rs.getString("fromID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setToID(rs.getString("toID").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				
				int chatTime = Integer.parseInt(rs.getString("chatTime").substring(11, 13));
				String timeType = "오전";
				
				if(chatTime >= 12) {
					timeType = "오후";
					chatTime -= 12;
				}
				chat.setChatTime(rs.getString("chatTime").substring(0, 11) + " " + timeType + " " + chatTime + " " + rs.getString("chatTime").substring(14, 16));
				chatList.add(chat);
			}
			for(int i = 0; i < chatList.size(); i++) {
				RChatDTO x = chatList.get(i);
				for(int j = 0; j < chatList.size(); j++) {
					RChatDTO y = chatList.get(j);
					if(x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
						if(x.getChatID() < y.getChatID()) {
							chatList.remove(x);
							i--;
							break;
						} else {
							chatList.remove(y);
							j--;
						}
					}
				}
			}
		} catch(Exception e) {
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
		return chatList; //리스트 반환
	}
	
	public int getUnreadChat(String fromID, String toID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String SQL = "SELECT COUNT(chatID) FROM RCHAT WHERE fromID = ? AND toID = ? AND chatRead = 0";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fromID);
			pstmt.setString(2, toID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("COUNT(chatID)");
			}
			return 0; //받은 메세지 없음
		} catch(Exception e) {
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
}