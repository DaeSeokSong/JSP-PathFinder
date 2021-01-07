package File;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/UploadAction")
public class UploadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
		int maxsize = 1024 * 1024 * 100;
		String encoding = "UTF-8";

		MultipartRequest multipartRequest = new MultipartRequest(request,savePath,maxsize,encoding,new DefaultFileRenamePolicy());
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		String fileName = multipartRequest.getOriginalFileName("file");
		String fileRealName = multipartRequest.getFilesystemName("file");
		if(fileName == null || fileName.equals("") || fileRealName == null || fileRealName.equals("")) {
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���ε� ������ �÷��ּ���.");
			response.sendRedirect("personalCloud.jsp");
			return;
		}else {
			String fileTime = formatter.format(new java.util.Date());
			new FileDAO().upload(fileName, fileRealName, fileTime); // DB�� ������ ���ε�
			request.getSession().setAttribute("messageType", "���� �޼���");
			request.getSession().setAttribute("messageContent", "���ε� ����");
			response.sendRedirect("personalCloud.jsp");
			return;
		}
	}

}
