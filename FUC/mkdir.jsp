<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.File" %>
<%@ page import="File.FileDAO" %>
<%@ page import="File.FileDTO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String)session.getAttribute("userID");
		}

		if (userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인이 필요합니다.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
	%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 파일 업로드</title>
	<meta name="viewport" content="width=device-width", initial-scale="1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<link rel="shortcut icon" href="images/favicon.ico">
</head>
<body>
	<form action="mkdir.jsp" method="post">
	<script language="javascript">
  	</script>
		<table class="table table-bordered table-hover"
			style="text-align: center; border: 1px solid #dddddd;">
			<tbody>
				<tr>
					<td style="width: 110px; padding-top: 14px;"><h5>폴더 생성</h5>
					<td colspan="2">
					<input name="dirname" type="text" value="new_name">
					<input type="submit" class="btn btn-primary" value="생성">
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<%
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("dirname");
		File f = new File(savePath + "/" + name);
		//String content = (String)request.getAttribute("content");
		if (name != null) {
			System.out.println(name + "생성 완료");
			//out.println("주소는" + content);
			if (!f.exists()) {
				f.mkdirs();
				FileDAO dao = new FileDAO();
				new FileDAO().createtable(name);
			}
		} else if (f.exists()) out.println("동일한 이름이 존재합니다.");
	%>

</body>
</html>