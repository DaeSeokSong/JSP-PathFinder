package tip;

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

@WebServlet("/TipWriteServlet")
public class TipWriteServlet extends HttpServlet {
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
			response.sendRedirect("acatipWrite.jsp");
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
		
		String tipTitle = multi.getParameter("tipTitle");
		String tipContent = multi.getParameter("tipContent");
		String tipLang = multi.getParameter("tipLang");
		if(tipTitle == null || tipTitle.equals("") || tipContent == null || tipContent.equals("")) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "내용을 모두 채워주세요.");
			response.sendRedirect("acatipWrite.jsp");
			return;
		}
		
		String tipFile = "";
		String tipRealFile = "";
		File file = multi.getFile("tipFile");
		if(file != null) {
			tipFile = multi.getOriginalFileName("tipFile");
			tipRealFile = file.getName();
		}
		TipDAO tipDAO = new TipDAO();
		tipDAO.write(userID, tipLang, tipTitle, tipContent, tipFile, tipRealFile);
		session.setAttribute("messageType", "성공 메세지");
		session.setAttribute("messageContent", "게시물 작성 완료.");
		response.sendRedirect("acatip.jsp");
		return;
	}

}
