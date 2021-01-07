<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<!DOCTYPE html>
<html>
<%
	String userID = null;
	if (session.getAttribute("userID") != null) {
		userID = (String) session.getAttribute("userID");
	}
	if (userID == null) {
		session.setAttribute("messageType", "오류 메세지");
		session.setAttribute("messageContent", "로그인이 필요합니다.");
		response.sendRedirect("login.jsp");
		return;
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
		function addBox(lastID, toID, chatContent, chatTime, unread, profile) {
			$('#boxTable').append('<tr onclick="window.open(\'O2Ochat.jsp?toID=' + encodeURIComponent(toID) + '\',\'FUChat\',\'width= 430px, height= 680px\')">' +
					'<td style="width: 150px;">' +
					'<img class="media-object img-circle" style="margin: 0 auto; width: 40px; height: 40px;" src="'+ profile +'">' +
					'<h5>' + lastID +
					'<span class="label label-info">' + unread + '</span></h5></td>' +
					'<td>' +
					'<h5 style="padding-top: 20px; margin-left: 155px;">' + chatContent +
					'<div class="pull-right" style="padding-top: 20px;">' + chatTime + '</div></h5>' +
					'</td>' +
					'</tr>');
		}
		function chatBoxFunction() {
			var userID = '<%= userID %>';
			$.ajax({
				type: "POST",
				url: "./rchatBox",
				data: {
					userID: encodeURIComponent(userID),
				},
				success: function(data) {
					if(data == "") return;
					$('#boxTable').html('');
					var parsed = JSON.parse(data);
					var result = parsed.result;
					for(var i = 0; i < result.length; i++) {
						if(result[i][0].value == userID) {
							result[i][0].value = result[i][1].value;
						} else {
							result[i][1].value = result[i][0].value;
						}
						addBox(result[i][0].value, result[i][1].value, result[i][2].value, result[i][3].value, result[i][4].value, result[i][5].value);
					}
				}
			});
		}
		function getInfiniteBox() {
			setInterval(function() {
				chatBoxFunction();
			}, 2000);
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
				<li><a href="jobs.jsp">일거리</a></li>
				<li><a href="find.jsp">친구찾기</a></li>
			</ul>
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
			<ul class="nav navbar-nav navbar-right">
				<li class="active"><a href="box.jsp">메세지 함<span id="unread" class="label label-info"></span></a></li>
				<li><a href="personalCloud.jsp">개인 작업실</a></li>
			</ul>
		</div>
	</nav>
	<div class="container">
		<table class="table" style="margin: 0 auto;">
			<thead>
				<tr>
					<th><h4>메세지 목록</h4></th>
				</tr>
			</thead>
		</table>
		<div style="overflow-y: auto; width: 100%; max-height: 450px;">
			<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd; margin: 0 auto;">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">친구</th>
						<th style="background-color: #eeeeee; text-align: center;">내용</th>
					</tr>
				</thead>
				<tbody id="boxTable">
				</tbody>
			</table>
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
				chatBoxFunction();
				getInfiniteUnread();
				getInfiniteBox();
			});
		</script>
	<%
		}
	%>
</body>
</html>