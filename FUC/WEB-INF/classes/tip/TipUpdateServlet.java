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

@WebServlet("/TipUpdateServlet")
public class TipUpdateServlet extends HttpServlet {
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
			response.sendRedirect("acatipUpdate.jsp");
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
		
		String tipID = multi.getParameter("tipID");
		if(tipID == null || tipID.equals("")) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����ϴ�.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		TipDAO tipDAO = new TipDAO();
		TipDTO tip = tipDAO.getTip(tipID);
		if(!userID.equals(tip.getUserID())) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ �� �����ϴ�.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		String tipTitle = multi.getParameter("tipTitle");
		String tipContent = multi.getParameter("tipContent");
		String tipLang = multi.getParameter("tipLang");
		if(tipTitle == null || tipTitle.equals("") || tipContent == null || tipContent.equals("")) {
			session.setAttribute("messageType", "���� �޼���");
			session.setAttribute("messageContent", "������ ��� ä���ּ���.");
			response.sendRedirect("acatipUpdate.jsp");
			return;
		}
		
		String tipFile = "";
		String tipRealFile = "";
		File file = multi.getFile("tipFile");
		if(file != null) {
			tipFile = multi.getOriginalFileName("tipFile");
			tipRealFile = file.getName();
			String prev = tipDAO.getRealFile(tipID);
			File prevFile = new File(savePath + "/" + prev);
			if(prevFile.exists()) {
				prevFile.delete();
			}
		} else {
			tipFile = tipDAO.getFile(tipID);
			tipRealFile = tipDAO.getRealFile(tipID);
		}
		tipDAO.update(tipID, tipLang, tipTitle, tipContent, tipFile, tipRealFile);
		session.setAttribute("messageType", "���� �޼���");
		session.setAttribute("messageContent", "�Խù� ���� �Ϸ�.");
		response.sendRedirect("acatip.jsp");
		return;
	}

}
