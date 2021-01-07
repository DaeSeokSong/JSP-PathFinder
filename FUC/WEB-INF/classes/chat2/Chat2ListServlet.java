package chat2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Chat2ListServlet")
public class Chat2ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String listType = request.getParameter("listType");
		
		if(listType == null || listType.equals("")) response.getWriter().write("");
		else if(listType.equals("today")) response.getWriter().write(getToday());
		else if(listType.equals("twoten")) response.getWriter().write(getTwoTen());
		else {
			try {
				Integer.parseInt(listType);
				response.getWriter().write(getID(listType));
			} catch (Exception e){
				response.getWriter().write("");
			}
		}
	}
	
	public String getToday() {
		StringBuffer result = new StringBuffer("");
		
		result.append("{\"result\":[");
		
		Chat2DAO chatDAO = new Chat2DAO();
		ArrayList<Chat2DTO> chatList = chatDAO.getChatList(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		
		for(int i = 0; i < chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		
		return result.toString();
	}
	
	public String getTwoTen() {
		StringBuffer result = new StringBuffer("");
		
		result.append("{\"result\":[");
		
		Chat2DAO chatDAO = new Chat2DAO();
		ArrayList<Chat2DTO> chatList = chatDAO.getChatListByRecent(20);
		
		for(int i = 0; i < chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		
		return result.toString();
	}
	
	public String getID(String chatID) {
		StringBuffer result = new StringBuffer("");
		
		result.append("{\"result\":[");
		
		Chat2DAO chatDAO = new Chat2DAO();
		ArrayList<Chat2DTO> chatList = chatDAO.getChatListByRecent(chatID);
		
		for(int i = 0; i < chatList.size(); i++) {
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime() + "\"}]");
			
			if(i != chatList.size() - 1) result.append(",");
		}
		result.append("], \"last\":\"" + chatList.get(chatList.size() - 1).getChatID() + "\"}");
		
		return result.toString();
	}
	
}
