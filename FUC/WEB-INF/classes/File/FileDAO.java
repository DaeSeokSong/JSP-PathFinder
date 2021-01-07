package File;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;

public class FileDAO { //데이터베이스

	private Connection conn;
	/*private Statement st; // 특정한 DB에 SQL문을 실행 시켜줌
	private ResultSet rs; // 실행된 결과를 받아옴
	*/
	public FileDAO() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String DBName = "dbuser";
			String dbURL = "jdbc:mysql://sampledb.cbnob5ipxlyn.ap-northeast-2.rds.amazonaws.com:3306/sampledb?useSSL=true&verifyServerCertificate=false";
			String dbPassword = "zxcvb12345";
			conn = DriverManager.getConnection(dbURL,DBName,dbPassword); 
			System.out.println("데이터 베이스  접속 성공.");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("데이터 베이스  접속 실패.");
		}
		}
	
	public int upload(String fileName, String fileRealName, String fileTime ) { // 파일 이름, 실제경로
		
		String SQL = "INSERT INTO FILE VALUES (?, ?, ?, 0)";
		try {
			//int nRowsInserted = 0;
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fileName);
			pstmt.setString(2, fileRealName);
			pstmt.setString(3, fileTime);
			
			//nRowsInserted += pstmt.executeUpdate();
			System.out.println("데이터 베이스 업로드 성공.");
			return pstmt.executeUpdate(); //DB에 실시간 업데이트
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("데이터 베이스 업로드 실패.");
		return -1;
	}
	
	public int hit (String fileRealName) { // 파일 이름, 실제경로
		String SQL = "UPDATE FILE SET downloadCount = downloadCount + 1 "
			+ "WHERE fileRealName = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fileRealName);	
			return pstmt.executeUpdate(); //DB에 실시간 업데이트
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("데이터 베이스 업로드 실패.");
		return -1;
	}
	
	public ArrayList<FileDTO>getList(String FILE){
		String SQL = "SELECT * FROM "+ FILE ;
		ArrayList<FileDTO> list = new ArrayList<FileDTO>();
		try {	
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				FileDTO file = new FileDTO(rs.getString(1),rs.getString(2),rs.getInt(4));
				list.add(file);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	public int createtable(String tName) {
	
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String DBName = "dbuser";
			String dbURL = "jdbc:mysql://sampledb.cbnob5ipxlyn.ap-northeast-2.rds.amazonaws.com:3306/sampledb?useSSL=true&verifyServerCertificate=false";
			String dbPassword = "zxcvb12345";
			conn = DriverManager.getConnection(dbURL,DBName,dbPassword); 
			System.out.println("데이터 베이스  접속 성공.");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("데이터 베이스  접속 실패.");
	}
		Statement stmt = null;
		String sql = null;
		String DBName = "sampledb";
		try {

			//데이터 베이스 생성 및 전환
			//String SQL1 = "use sampledb";
			//stmt.executeUpdate(SQL1);;
			//CreateOrChangeDatabase(DBName);

			//information_schema.tables 로 부터 테이블의 존재 유무 확인

			//String tableSql = "SELECT table_name FROM information_schema.tables where table_schema = ? and table_name = ?";

			//pstmt.setString(1, DBName);

			//pstmt.setString(2, tName);

			//ResultSet rs = pstmt.executeQuery();

			

			//테이블이 없다면 테이블 생성
			 sql = "create table "+tName

							+ "("
							+ "fileName varchar(200),"
							+ "fileRealname varchar(200),"
							+ "fileTime varchar(200) DEFAULT '0000-00-00 00:00:00',"
							+ "downloadCount int(11)"
							+ ")";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			//ResultSet rs2 = pstmt.executeQuery(sql);

//				stmt.close();

				//System.out.println(rs2);

			} catch (Exception e) {

			System.out.println("CreateTable err : " + e);

		} finally {

			try{

				if(stmt!=null)stmt.close();

				if(conn!=null)conn.close();

			} catch (Exception e) {

			}

		}
	/*try {
		String SQL = "use sampledb";
		stmt = conn.createStatement();
		stmt.executeUpdate(SQL);
		String tableSql = "SELECT table_name FROM information_schema.tables where table_schema = ? and table_name = ?";
		PreparedStatement pstmt = conn.prepareStatement(tableSql);
		pstmt.setString(1, DBName);
		pstmt.setString(2, tName);

		ResultSet rs1 = pstmt.executeQuery();
		System.out.println(rs1);
		sql = "create table "+tName
						+"("
						+ "num_id int not null primary key auto_increment,"
						+ "fileName varchar(200),"
						+ "fileRealname varchar(200),"
						+ "fileTime varchar(200) DEFAULT 0000-00-00 00:00:00,"
						+ "downloadCount int(11))";
			
			stmt.executeUpdate(sql);
			ResultSet rs2 = stmt.executeQuery(sql);
			System.out.println(rs2);
			System.out.println("데이터 테이블 생성 완료.");
	} catch (Exception e) {

		System.out.println("CreateTable err : " + e);

	}finally {
	try {
		stmt.close();
	}catch(SQLException e) {
		e.printStackTrace();
	}
	try {
		con.close();
	}catch(SQLException e) {
		e.printStackTrace();
	}
	}*/
	System.out.println("데이터 테이블 생성 완료");
	return 1;
	} 	
}
	/* 업로드가 안될시
	C:\Users\bbaej\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\File Upload
	해당링크(bbaej->유저명)에 들어가 upload파일을 생성해줄것.
	*/
	
