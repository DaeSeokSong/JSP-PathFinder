<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="jobs.JobsDAO" %>
<%@ page import="jobs.JobsDTO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		
		String pageNumber = "1";
		if(request.getParameter("pageNumber") != null) {
			pageNumber = request.getParameter("pageNumber");
		}
		
		try {
			Integer.parseInt(pageNumber);
		} catch(Exception e) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "페이지 번호가 잘못되었습니다.");
			response.sendRedirect("jobs.jsp");
			return;
		}
		ArrayList<JobsDTO> jobsList = new JobsDAO().getList(pageNumber);
	%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width", initial-scale="1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<link rel="shortcut icon" href="images/favicon.ico">
<title>FUC</title>
<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.js"></script>
<style type="text/css">
	a, a:hover{
		color: #000000;
		text-decoration: none;
	}
</style>
	<script type="text/javascript">
		function getUnread() {
			$.ajax({
				type: "POST",
				url: "./rchatUnread",
				data: {
					userID: encodeURIComponent('<%= userID %>'),
				},
				success: function(result) {
					if(result >= 1) {
						showUnread(result);
					} else {
						showUnread('');
					}
				}
			});
		}
		function getInfiniteUnread() {
			setInterval(function() {
				getUnread();
			}, 2000);
		}
		function showUnread(result) {
			$('#unread').html(result);
		}
	</script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">For User's Compile</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">FUC</a></li>
				<li><a href="academy.jsp">FUCademy</a></li>
				<li><a href="bbs.jsp">FUQuestion</a></li>
				<li><a href="community.jsp">FUCommunity</a></li>
				<li class="active"><a href="jobs.jsp">일거리</a></li>
				<li><a href="find.jsp">친구찾기</a></li>
			</ul>
			<%
				if(userID == null){
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">로그인<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>
			</ul>
			<%
				} else{
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="update.jsp">회원정보수정</a></li>
						<li><a href="profileUpdate.jsp">프로필 수정</a></li>
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%
				}
			%>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="box.jsp">메세지 함<span id="unread" class="label label-info"></span></a></li>
				<li><a href="personalCloud.jsp">개인 작업실</a></li>
			</ul>
		</div>
	</nav>
	<div class="container">
		<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd">
			<thead>
				<tr>
					<th colspan="5"><h4>구인/구직 게시판</h4></th>
				</tr>
				<tr>
					<th style="background-color: #fafafa; color: #000000; width: 100px;"><h5>기반언어</h5></th>
					<th style="background-color: #fafafa; color: #000000;"><h5>제목</h5></th>
					<th style="background-color: #fafafa; color: #000000; width: 200px;"><h5>작성자</h5></th>
					<th style="background-color: #fafafa; color: #000000; width: 100px;"><h5>작성일</h5></th>
					<th style="background-color: #fafafa; color: #000000; width: 70px;"><h5>조회수</h5></th>
				</tr>
			</thead>
			<tbody>
				<%
					for (int i = 0; i < jobsList.size(); i++) {
						JobsDTO jobs = jobsList.get(i);
				%>
				<tr>
					<td><%=jobs.getJobLang()%></td>
					<td style="text-align: left;"><a href="jobsShow.jsp?jobID=<%=jobs.getJobID()%>">
				<%
					for(int j = 0; j < jobs.getJobLevel(); j++) {
				%>
						<span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>
				<%
					}
				%>
				<%
					if(jobs.getJobAvailable() == 0) {
				%>
						(삭제된 게시물)
				<%
					} else {
				%>
						<%= jobs.getJobTitle() %>
				<%
					}
				%>
					</a></td>
						<td><%=jobs.getUserID()%></td>
						<td><%=jobs.getJobDate()%></td>
						<td><%=jobs.getJobHit()%></td>
					</tr>
				<%
					}
				%>
				<tr>
					<td colspan="5">
						<a href="jobsWrite.jsp" class="btn btn-primary pull-right" type="submit">글쓰기</a>
						<ul class="pagination" style="margin: 0 auto;">
						<%
							int startPage = (Integer.parseInt(pageNumber) / 10) * 10 + 1;
							if(Integer.parseInt(pageNumber) % 10 == 0) startPage -= 10;
							
							int targetPage = new JobsDAO().targetPage(pageNumber);
							
							if(startPage != 1) {
						%>
							<li><a href="jobs.jsp?pageNumber=<%= startPage - 10 %>"><span class="glyphicon glyphicon-chevron-left"></span></a></li>
						<%
							} else {
						%>
							<li><span class="glyphicon glyphicon-chevron-left" style="color: gray;"></span></li>
						<%
							}
							for(int i = startPage; i < Integer.parseInt(pageNumber); i++) {
						%>
							<li><a href="jobs.jsp?pageNumber=<%= i %>"><%= i %></a></li>
						<%
							}
						%>
							<li class="active"><a href="jobs.jsp?pageNumber=<%= pageNumber %>"><%= pageNumber %></a></li>
						<%
							for(int i = Integer.parseInt(pageNumber) + 1; i <= targetPage + Integer.parseInt(pageNumber); i++) {
								if(i < startPage + 10) {
						%>
							<li><a href="jobs.jsp?pageNumber=<%= i %>"><%= i %></a></li>
						<%
								}
							}
							if(targetPage + Integer.parseInt(pageNumber) > startPage + 9) {
						%>
							<li><a href="jobs.jsp?pageNumber=<%= startPage + 10 %>"><span class="glyphicon glyphicon-chevron-right"></span></a></li>
						<%
							} else {
						%>
							<li><span class="glyphicon glyphicon-chevron-right" style="color: gray;"></span></li>
						<%
							}
						%>
						</ul>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<%
		String messageContent = null;
		if (session.getAttribute("messageContent") != null) {
			messageContent = (String) session.getAttribute("messageContent");
		}

		String messageType = null;
		if (session.getAttribute("messageType") != null) {
			messageType = (String) session.getAttribute("messageType");
		}

		if (messageContent != null) {
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog"
		aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div
					class="modal-content <%if (messageType.equals("오류 메세지"))
					out.println("panel-warning");
				else
					out.println("panel-success");%>">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span> <span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%=messageType%>
						</h4>
					</div>
					<div class="modal-body">
						<%=messageContent%>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" data-dismiss="modal">확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%
		session.removeAttribute("messageContent");
		session.removeAttribute("messageType");
		}
	%>
	<script>
		$('#messageModal').modal("show");
	</script>
	<%
		if(userID != null) {	
	%>
		<script type="text/javascript">
			$(document).ready(function() {
				getUnread();
				getInfiniteUnread();
			});
		</script>
	<%
		}
	%>
	<script type="text/javascript">
		$(document).on('click', '.browse', function() {
			var file = $(this).parent().parent().parent().find('.file');
			file.trigger('click');
		});
		$(document).on('change', '.file', function() {
			$(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
		});
	</script>
</body>
</html>