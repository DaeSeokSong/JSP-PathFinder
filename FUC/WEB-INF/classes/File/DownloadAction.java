package File;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadAction")
public class DownloadAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String root = request.getSession().getServletContext().getRealPath("/");
		String savePath = root + "upload";
		String fileRealName = request.getParameter("file");
		String nowdir = request.getParameter("content");
		//if(fileRealName==null) directory = nowdir;
		
		File file = new File(savePath + "/" + fileRealName);
		
		if(file.isDirectory()) {
			response.setCharacterEncoding("UTF-8");
			System.out.println("���丮�Դϴ�.");
			String newdir = savePath + "/" + fileRealName;
			//String newdir_sub = request.getParameter(newdir);    
		    request.setAttribute("content", newdir);
		    request.setAttribute("name", fileRealName);
		    ServletContext context =getServletContext();
		    RequestDispatcher dispatcher = context.getRequestDispatcher("/personalCloud.jsp"); //�ѱ� ������ �ּ�
		    dispatcher.forward(request, response);
			}else {
		String mimeType = getServletContext().getMimeType(file.toString());
		if (mimeType == null) {
			response.setContentType("application/octet-stream"); //���� ���� ������ �ְ� �ޱ�	
		}
		String downloadName = null;
		if (request.getHeader("user-agent").indexOf("MSHTML") == -1) { //MSIE = �ͽ��÷ξ�
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileRealName.getBytes("KSC5601"), "ISO8859_1")); //8859 ���ڵ��� ���ڰ� ���� Ȯ�� �پ���
		} else {
			downloadName = new String(fileRealName.getBytes("UTF-8"), "iso-8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=\"" + downloadName + "\";"); //��� �Ӽ� ��� ó��
			response.setHeader("Content-Type", "application/octet-stream; charset=UTF-8");
		}
		
		FileInputStream fileInputStream = new FileInputStream(file); //inputstream �� ��´�.
		ServletOutputStream servletOutputStream = response.getOutputStream();
		
		byte b[] = new byte[1024]; //�����͸� �ɰ� 1024byte ��ŭ ������.
		int data = 0;
		
		while ((data = (fileInputStream.read(b, 0, b.length))) != -1) {
			servletOutputStream.write(b,0,data);
		}

		new FileDAO().hit(fileRealName);
		
		servletOutputStream.flush();
		servletOutputStream.close();
		fileInputStream.close();
		}
	}
	
}

