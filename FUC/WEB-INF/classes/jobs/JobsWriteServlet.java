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

@WebServlet("/JobsWriteServlet")
public class JobsWriteServlet extends HttpServlet {
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
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "파일 크기는 10MB를 넘을 수 없습니다.");
			response.sendRedirect("jobsWrite.jsp");
			return;
		}
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		if(!userID.equals((String)session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		String jobTitle = multi.getParameter("jobTitle");
		String jobContent = multi.getParameter("jobContent");
		String jobLang = multi.getParameter("jobLang");
		if(jobTitle == null || jobTitle.equals("") || jobContent == null || jobContent.equals("")) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "내용을 모두 채워주세요.");
			response.sendRedirect("jobsWrite.jsp");
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
		jobDAO.write(userID, jobLang, jobTitle, jobContent, jobFile, jobRealFile);
		session.setAttribute("messageType", "성공 메세지");
		session.setAttribute("messageContent", "게시물 작성 완료.");
		response.sendRedirect("jobs.jsp");
		return;
	}

}
