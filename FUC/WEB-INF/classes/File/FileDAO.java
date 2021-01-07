package File;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;

public class FileDAO { //�����ͺ��̽�

	private Connection conn;
	/*private Statement st; // Ư���� DB�� SQL���� ���� ������
	private ResultSet rs; // ����� ����� �޾ƿ�
	*/
	public FileDAO() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String DBName = "dbuser";
			String dbURL = "jdbc:mysql://sampledb.cbnob5ipxlyn.ap-northeast-2.rds.amazonaws.com:3306/sampledb?useSSL=true&verifyServerCertificate=false";
			String dbPassword = "zxcvb12345";
			conn = DriverManager.getConnection(dbURL,DBName,dbPassword); 
			System.out.println("������ ���̽�  ���� ����.");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("������ ���̽�  ���� ����.");
		}
		}
	
	public int upload(String fileName, String fileRealName, String fileTime ) { // ���� �̸�, �������
		
		String SQL = "INSERT INTO FILE VALUES (?, ?, ?, 0)";
		try {
			//int nRowsInserted = 0;
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fileName);
			pstmt.setString(2, fileRealName);
			pstmt.setString(3, fileTime);
			
			//nRowsInserted += pstmt.executeUpdate();
			System.out.println("������ ���̽� ���ε� ����.");
			return pstmt.executeUpdate(); //DB�� �ǽð� ������Ʈ
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("������ ���̽� ���ε� ����.");
		return -1;
	}
	
	public int hit (String fileRealName) { // ���� �̸�, �������
		String SQL = "UPDATE FILE SET downloadCount = downloadCount + 1 "
			+ "WHERE fileRealName = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, fileRealName);	
			return pstmt.executeUpdate(); //DB�� �ǽð� ������Ʈ
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("������ ���̽� ���ε� ����.");
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
			System.out.println("������ ���̽�  ���� ����.");
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("������ ���̽�  ���� ����.");
	}
		Statement stmt = null;
		String sql = null;
		String DBName = "sampledb";
		try {

			//������ ���̽� ���� �� ��ȯ
			//String SQL1 = "use sampledb";
			//stmt.executeUpdate(SQL1);;
			//CreateOrChangeDatabase(DBName);

			//information_schema.tables �� ���� ���̺��� ���� ���� Ȯ��

			//String tableSql = "SELECT table_name FROM information_schema.tables where table_schema = ? and table_name = ?";

			//pstmt.setString(1, DBName);

			//pstmt.setString(2, tName);

			//ResultSet rs = pstmt.executeQuery();

			

			//���̺��� ���ٸ� ���̺� ����
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
			System.out.println("������ ���̺� ���� �Ϸ�.");
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
	System.out.println("������ ���̺� ���� �Ϸ�");
	return 1;
	} 	
}
	/* ���ε尡 �ȵɽ�
	C:\Users\bbaej\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\File Upload
	�ش縵ũ(bbaej->������)�� �� upload������ �������ٰ�.
	*/
	
