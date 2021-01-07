package jobs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JobsDAO {
	
	private DataSource dataSource;

	public JobsDAO() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/FUC");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int write(String userID, String jobLang, String jobTitle, String jobContent, String jobFile, String jobRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO JOB SELECT ?, IFNULL((SELECT MAX(jobID) + 1 FROM JOB), 1), ?, ?, now(), ?, 0, ?, ?, IFNULL((SELECT MAX(jobGroup) + 1 FROM JOB), 0), 0, 0, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, jobTitle);
			pstmt.setString(3, jobContent);
			pstmt.setString(4, jobLang);
			pstmt.setString(5, jobFile);
			pstmt.setString(6, jobRealFile);
			
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
	
	public JobsDTO getJob(String jobID) {
		JobsDTO job = new JobsDTO();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM JOB WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, jobID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				job.setUserID(rs.getString("userID"));
				job.setJobID(rs.getInt("jobID"));
				job.setJobTitle(rs.getString("jobTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				job.setJobContent(rs.getString("jobContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				job.setJobDate(rs.getString("jobDate").substring(0, 11));
				job.setJobLang(rs.getString("jobLang"));
				job.setJobHit(rs.getInt("jobHit"));
				job.setJobFile(rs.getString("jobFile"));
				job.setJobRealFile(rs.getString("jobRealFile"));
				job.setJobGroup(rs.getInt("jobGroup"));
				job.setJobSequence(rs.getInt("jobSequence"));
				job.setJobLevel(rs.getInt("jobLevel"));
				job.setJobAvailable(rs.getInt("jobAvailable"));
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
		return job;
	}
	
	public ArrayList<JobsDTO> getList(String pageNumber) {
		ArrayList<JobsDTO> jobList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM JOB WHERE jobGroup > (SELECT MAX(jobGroup) FROM JOB) - ? AND jobGroup <= (SELECT MAX(jobGroup) FROM JOB) - ? ORDER BY jobGroup DESC, jobSequence ASC";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(pageNumber) * 10);
			pstmt.setInt(2, (Integer.parseInt(pageNumber) - 1) * 10);
			rs = pstmt.executeQuery();
			jobList = new ArrayList<JobsDTO>();
			while(rs.next()) {
				JobsDTO job = new JobsDTO();
				job.setUserID(rs.getString("userID"));
				job.setJobID(rs.getInt("jobID"));
				job.setJobTitle(rs.getString("jobTitle").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				job.setJobContent(rs.getString("jobContent").replaceAll(" ","&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
				job.setJobDate(rs.getString("jobDate").substring(0, 11));
				job.setJobLang(rs.getString("jobLang"));
				job.setJobHit(rs.getInt("jobHit"));
				job.setJobFile(rs.getString("jobFile"));
				job.setJobRealFile(rs.getString("jobRealFile"));
				job.setJobGroup(rs.getInt("jobGroup"));
				job.setJobSequence(rs.getInt("jobSequence"));
				job.setJobLevel(rs.getInt("jobLevel"));
				job.setJobAvailable(rs.getInt("jobAvailable"));
				jobList.add(job);
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
		return jobList;
	}
	
	public int hit(String jobID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE JOB SET jobHit = jobHit + 1 WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, jobID);
			
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
	
	public String getFile(String jobID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT jobFile FROM JOB WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, jobID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("jobFile");
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
	
	public String getRealFile(String jobID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String SQL = "SELECT jobRealFile FROM JOB WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, jobID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString("jobRealFile");
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
	
	public int update(String jobID, String jobLang, String jobTitle, String jobContent, String jobFile, String jobRealFile) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE JOB SET jobTitle = ?, jobContent = ?, jobLang = ?, jobFile = ?, jobRealFile = ? WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, jobTitle);
			pstmt.setString(2, jobContent);
			pstmt.setString(3, jobLang);
			pstmt.setString(4, jobFile);
			pstmt.setString(5, jobRealFile);
			pstmt.setInt(6, Integer.parseInt(jobID));
			
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
	
	public int delete(String jobID) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE JOB SET jobAvailable = 0 WHERE jobID = ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(jobID));
			
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
	
	public int reply(String userID, String jobLang, String jobTitle, String jobContent, String jobFile, String jobRealFile, JobsDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "INSERT INTO JOB SELECT ?, IFNULL((SELECT MAX(jobID) + 1 FROM JOB), 1), ?, ?, now(), ?, 0, ?, ?, ?, ?, ?, 1";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.setString(2, jobTitle);
			pstmt.setString(3, jobContent);
			pstmt.setString(4, jobLang);
			pstmt.setString(5, jobFile);
			pstmt.setString(6, jobRealFile);
			pstmt.setInt(7, parent.getJobGroup());
			pstmt.setInt(8, parent.getJobSequence() + 1);
			pstmt.setInt(9, parent.getJobLevel() + 1);
			
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
	
	public int replyUpdate(JobsDTO parent) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String SQL = "UPDATE JOB SET jobSequence = jobSequence + 1 WHERE jobGroup = ? AND jobSequence > ?";
		try {
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, parent.getJobGroup());
			pstmt.setInt(2, parent.getJobSequence());
			
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
		String SQL = "SELECT * FROM JOB WHERE jobGroup >= ?";
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
		String SQL = "SELECT COUNT(jobGroup) FROM JOB WHERE jobGroup > ?";
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
