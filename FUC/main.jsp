<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<%
	String userID = null;
	if(session.getAttribute("userID") != null){
		userID = (String)session.getAttribute("userID");
	}
	
	String userProfile = new UserDAO().getProfile(userID);
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
				<li class="active"><a href="main.jsp">FUC</a></li>
				<li><a href="academy.jsp">FUCademy</a></li>
				<li><a href="bbs.jsp">FUQuestion</a></li>
				<li><a href="community.jsp">FUCommunity</a></li>
				<li><a href="jobs.jsp">일거리</a></li>
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
		<div class="jumbotron pull-left" style="background-color: white; width: 200px; height: auto; border: 1px solid black; text-align: center;">
			<img class="media-object img-circle" style="width: 30px; height: 30px; margin: 0 auto;" src=<%= userProfile %> alt="">
			<% 
				if(userID != null) {
					String userName = new UserDAO().getUserName(userID);
			%>
				<div style="text-align: center;"><h3><%= userName %></h3></div>
				<h5 style="color: green">학생 Lv. 1</h5>
			<% 
				} else {
			%>
				<h4>익명</h4>
			<%
				}
			%>
			<br>
			<hr>
			<h3>친구</h3>
			<br>
			<hr>
			<h3>설정</h3>
			<h5 style="color: green">자격신청</h5>
			<h5 style="color: green">알림</h5>
		</div>
		<div class="jumbotron pull-right" style="background-color: white; width: 900px; height: auto; border: 1px solid black; text-align: center;">
			<h2>타임라인</h2>
		</div>
		<div class="jumbotron pull-right" style="background-color: white; width: 900px; height: auto; border: 1px solid black; text-align: center;">
			<h2>타임라인</h2>
		</div>
		<div class="jumbotron pull-right" style="background-color: white; width: 900px; height: auto; border: 1px solid black; text-align: center;">
			<h2>타임라인</h2>
		</div>
	</div>
	<div class="container">
		<div class="jumbotron">
			<h1>웹 사이트 소개</h1>
			<p>FUC은 For User's Compile의 줄임말입니다.</p>
			<p>개발자들을 위한 자신들의 프로그램 버전을 관리해줄 클라우드, 질문공간, 학습까지 담당하는 프로그래머를 위한 SNS형 플랫폼입니다.</p>
			<p>FUCademy = 코딩을 배우거나 가르치는 학습을 위한 공간</p>
			<p>FUQuestion = 코딩이나 오류에 대한 질문을 위한 게시판</p>
			<p>FUCommunity = 코딩 외에 여러 이야기를 하거나 정보를 얻는 장소</p>
			<p>일자리 = 일자리 또는 알바 구하거나, 구인하는 게시판</p>
			<p>메세지함 = 친구와의 1:1 채팅</p>
			<p>개인 작업실 = 개인 클라우드 겸 프로젝트, 친구, 구독한 사람 목록이 있는 곳</p>
		</div>
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
</body>
</html>