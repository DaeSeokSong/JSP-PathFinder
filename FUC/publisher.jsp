<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <style type="text/css" media="screen">
            html, body, #containerA, #containerB { height:100%; }
            body { margin:0; padding:0; overflow:hidden; }
        </style>
        <title>FUCademy 실시간 강의 설정창</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <script type="text/javascript" src="assets/swfobject.js"></script>
        <script type="text/javascript">
        var flashvars = {};
        var params = {allowfullscreen: "true"};
        var attributes = {};
        swfobject.embedSWF("publisher.swf", "myContent", "100%", "100%", "9.0.0", "assets/expressInstall.swf", flashvars, params, attributes);
        </script>
    </head>
    <body>
   		<%
   			String userID = null;
   		
			if(session.getAttribute("userID") != null){
				userID = (String)session.getAttribute("userID");
			}
			
			if(userID == null){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('로그인이 필요합니다.')");
				script.println("location.href = 'login.jsp'");
				script.println("</script>");
			}
		%>
        <div id="myContent">
            <h1>플레시 플레이어가 필요합니다.</h1>
        </div>
    </body>
    
</html>