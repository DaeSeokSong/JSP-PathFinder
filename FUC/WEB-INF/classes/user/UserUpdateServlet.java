package user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserUpdateServlet")
public class UserUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		String userID = request.getParameter("userID");
		String userPassword1 = request.getParameter("userPassword1");
		String userPassword2 = request.getParameter("userPassword2");
		String userName = request.getParameter("userName");
		String userGender = request.getParameter("userGender");
		String userEmail = request.getParameter("userEmail");
		
		if(userID == null || userID.equals("")
				|| userPassword1 == null || userPassword1.equals("")
				|| userPassword2 == null || userPassword2.equals("")
				|| userName == null || userName.equals("")
				|| userGender == null || userGender.equals("")
				|| userEmail == null || userEmail.equals("")) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "모든 내용을 입력하세요.");
			response.sendRedirect("update.jsp");
			return;
		}
		if(!userID.equals((String)session.getAttribute("userID"))) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		if(!userPassword1.equals(userPassword2)) {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "비밀번호가 서로 다릅니다.");
			response.sendRedirect("update.jsp");
			return;
		}
		
		int result = new UserDAO().update(userID, userPassword1, userName, userGender, userEmail);
		if(result == 1) {
			request.getSession().setAttribute("messageType", "성공 메세지");
			request.getSession().setAttribute("messageContent", "수정 완료");
			request.getSession().setAttribute("userID", userID);
			response.sendRedirect("main.jsp");
		}else {
			request.getSession().setAttribute("messageType", "오류 메세지");
			request.getSession().setAttribute("messageContent", "ERROR : 데이터 베이스 오류 발생.");
			response.sendRedirect("update.jsp");
		}
	}

}
