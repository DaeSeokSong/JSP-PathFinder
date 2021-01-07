package jobs;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/JobsReplyServlet")
public class JobsReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024;
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
		
		try {
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch(Exception e) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���� ũ��� 10MB�� ���� �� �����ϴ�.");
			response.sendRedirect("jobsReply.jsp");
			return;
		}
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		if(!userID.equals((String)session.getAttribute("userID"))) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����ϴ�.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		String jobID = multi.getParameter("jobID");
		if(jobID == null || jobID.equals("")) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����ϴ�.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		String jobTitle = multi.getParameter("jobTitle");
		String jobContent = multi.getParameter("jobContent");
		String jobLang = multi.getParameter("jobLang");
		if(jobTitle == null || jobTitle.equals("") || jobContent == null || jobContent.equals("")) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ ��� ä���ּ���.");
			response.sendRedirect("jobsReply.jsp");
			return;
		}
		
		String jobFile = "";
		String jobRealFile = "";
		File file = multi.getFile("jobFile");
		if(file != null) {
			jobFile = multi.getOriginalFileName("jobFile");
			jobRealFile = file.getName();
		}
		JobsDAO jobDAO = new JobsDAO();
		JobsDTO parent = jobDAO.getJob(jobID);
		jobDAO.replyUpdate(parent);
		jobDAO.reply(userID, jobLang, jobTitle, jobContent, jobFile, jobRealFile, parent);
		session.setAttribute("messageType", "���� �޼���");
		session.setAttribute("messageContent", "�亯 �ۼ� �Ϸ�.");
		response.sendRedirect("jobs.jsp");
		return;
	}

}
