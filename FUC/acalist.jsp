<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
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
				<li><a href="main.jsp">FUC</a></li>
				<li class="active"><a href="academy.jsp">FUCademy</a></li>
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
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-2"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="academy.jsp" style="background: #000000;">Academy</a>
		</div>
		<div class="collapse navbar-collapse" style="background: #000000;" id="bs-example-navbar-collapse-2">
			<ul class="nav navbar-nav">
				<li><a href="academy.jsp">실시간 강좌</a></li>
				<li><a href="acabbs.jsp">게시판형 강좌</a></li>
				<li class="active"><a href="acalist.jsp">강사 리스트</a></li>
				<li><a href="acatip.jsp">팁 & 프로젝트</a></li>
			</ul>
		</div>
	</nav>
	<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd">
		<thead>
			<tr>
				<th colspan="5"><h4>강사 목록</h4></th>
			</tr>
			<tr>
				<th style="background-color: #fafafa; color: #000000;">강사명</th>
				<th style="background-color: #fafafa; color: #000000;">전문 프로그래밍 언어</th>
				<th style="background-color: #fafafa; color: #000000;">지명도</th>
				<th style="background-color: #fafafa; color: #000000;">평가</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td style="color: yellow; background-color: black;">김대엽 교수님</td><!-- 데모 때 보여주기 용으로 대충 만든 것 -->
				<td>All</td>
				<td>매우 좋음</td>
				<td>매우 좋음</td>
			</tr>
			<tr>
				<td style="color: green">송대석</td><!-- 데모 때 보여주기 용으로 대충 만든 것 -->
				<td>JSP, Java, C, C++</td>
				<td>좋음</td>
				<td>나쁨</td>
			</tr>
			<tr>
				<td style="color: blue;">배시현</td><!-- 데모 때 보여주기 용으로 대충 만든 것 -->
				<td>미정</td>
				<td>보통</td>
				<td>보통</td>
			</tr>
			<tr>
				<td>손민성</td><!-- 데모 때 보여주기 용으로 대충 만든 것 -->
				<td>미정</td>
				<td>보통</td>
				<td>보통</td>
			</tr>
		</tbody>
	</table>
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