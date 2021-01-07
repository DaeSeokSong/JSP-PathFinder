package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	private DataSource dataSource;
	
	public BoardDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int write(String userID, String boardLang, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO BOARD SELECT ?, IFNULL((SELECT MAX(boardID) + 1 FROM BOARD), 1), ?, ?, now(), ?, 0, ?, ?, IFNULL((SELECT MAX(boardGroup) + 1 FROM BOARD), 0), 0, 0, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent);
			pstmt.setString(4, boardLang);
			pstmt.setString(5, boardFile);
			pstmt.setString(6, boardRealFile);
			
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
	
	public BoardDTO getBoard(String boardID) {
		BoardDTO board = new BoardDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM BOARD WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardContent(rs.getString("boardContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardDate(rs.getString("boardDate").substring(0, 11));
				board.setBoardLang(rs.getString("boardLang"));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardGroup(rs.getInt("boardGroup"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));
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
		return board;
	}
	
	public ArrayList<BoardDTO> getList(String pageNumber) {
		ArrayList<BoardDTO> boardList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM BOARD WHERE boardGroup > (SELECT MAX(boardGroup) FROM BOARD) - ? AND boardGroup <= (SELECT MAX(boardGroup) FROM BOARD) - ? ORDER BY boardGroup DESC, boardSequence ASC";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			pstmt.setInt(2, (Integer.parseInt(pageNumber) - 1) * 10);
			rs = pstmt.executeQuery();
			boardList = new ArrayList<BoardDTO>();
			while(rs.next()) {
				BoardDTO board = new BoardDTO();
				board.setUserID(rs.getString("userID"));
				board.setBoardID(rs.getInt("boardID"));
				board.setBoardTitle(rs.getString("boardTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardContent(rs.getString("boardContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				board.setBoardDate(rs.getString("boardDate").substring(0, 11));
				board.setBoardLang(rs.getString("boardLang"));
				board.setBoardHit(rs.getInt("boardHit"));
				board.setBoardFile(rs.getString("boardFile"));
				board.setBoardRealFile(rs.getString("boardRealFile"));
				board.setBoardGroup(rs.getInt("boardGroup"));
				board.setBoardSequence(rs.getInt("boardSequence"));
				board.setBoardLevel(rs.getInt("boardLevel"));
				board.setBoardAvailable(rs.getInt("boardAvailable"));
				boardList.add(board);
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
		return boardList;
	}
	
	public int hit(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE BOARD SET boardHit = boardHit + 1 WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			
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
	
	public String getFile(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT boardFile FROM BOARD WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("boardFile");
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
	
	public String getRealFile(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT boardRealFile FROM BOARD WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("boardRealFile");
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
	
	public int update(String boardID, String boardLang, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE BOARD SET boardTitle = ?, boardContent = ?, boardLang = ?, boardFile = ?, boardRealFile = ? WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, boardTitle);
			pstmt.setString(2, boardContent);
			pstmt.setString(3, boardLang);
			pstmt.setString(4, boardFile);
			pstmt.setString(5, boardRealFile);
			pstmt.setInt(6, Integer.parseInt(boardID));
			
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
	
	public int delete(String boardID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE BOARD SET boardAvailable = 0 WHERE boardID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(boardID));
			
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
	
	public int reply(String userID, String boardLang, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO BOARD SELECT ?, IFNULL((SELECT MAX(boardID) + 1 FROM BOARD), 1), ?, ?, now(), ?, 0, ?, ?, ?, ?, ?, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, boardTitle);
			pstmt.setString(3, boardContent);
			pstmt.setString(4, boardLang);
			pstmt.setString(5, boardFile);
			pstmt.setString(6, boardRealFile);
			pstmt.setInt(7, parent.getBoardGroup());
			pstmt.setInt(8, parent.getBoardSequence() + 1);
			pstmt.setInt(9, parent.getBoardLevel() + 1);
			
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
	
	public int replyUpdate(BoardDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE BOARD SET boardSequence = boardSequence + 1 WHERE boardGroup = ? AND boardSequence > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getBoardGroup());
			pstmt.setInt(2, parent.getBoardSequence());
			
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
		String SQL = "SELECT * FROM BOARD WHERE boardGroup >= ?";
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
		String SQL = "SELECT COUNT(boardGroup) FROM BOARD WHERE boardGroup > ?";
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
