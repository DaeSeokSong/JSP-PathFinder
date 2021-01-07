package tip;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/TipDeleteServlet")
public class TipDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		String userID = (String)session.getAttribute("userID");
		String tipID = request.getParameter("tipID");
		
		if(tipID == null || tipID.equals("")) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("acatip.jsp");
			return;
		}
		
		TipDAO tipDAO = new TipDAO();
		TipDTO tip = tipDAO.getTip(tipID);
		if(!userID.equals(tip.getUserID())) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("acatip.jsp");
			return;
		}
		
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
		String prev = tipDAO.getRealFile(tipID);
		int result = tipDAO.delete(tipID);
		if(result == -1) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("acatip.jsp");
			return;
		} else {
			File prevFile = new File(savePath + "/" + prev);
			if(prevFile.exists()) {
				prevFile.delete();
			}
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "삭제 성공");
			response.sendRedirect("acatip.jsp");
		}
	}

}
