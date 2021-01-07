<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="user.UserDAO" %>
<%@ page import="user.UserDTO" %>
<%@ page import="java.util.ArrayList" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		String profile = "";
		String user = "";
		ArrayList<UserDTO> profileList = new UserDAO().getProfileList();
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<script type="text/javascript" src="swfobject.js"></script>
	<script type="text/javascript">
		swfobject.registerObject("myContent", "9.0.0", "assets\expressInstall.swf");
	</script>
	<meta name="viewport" content="width=device-width", initial-scale="1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/chatcustom.css">
	<link rel="shortcut icon" href="images/favicon.ico">
	<title>FUC</title>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
	<script type="text/javascript">
		var lastID = 0;
		function autoClosingAlert(selector, delay) {
			var alert = $(selector).alert();
			
			alert.show();
			window.setTimeout(function() {alert.hide()}, delay);
		}
		function submitFunction() {
			var chatName = $('#chatName').val();
			var chatContent = $('#chatContent').val();
			
			$.ajax({
				type: "POST",
				url: "./chat2SubmitServlet",
				data: {
					chatName: encodeURIComponent(chatName),
					chatContent: encodeURIComponent(chatContent)
				},
				success: function(result) {
					if(result == 1){
						autoClosingAlert('#successMessage', 2000);
					}else if(result == 0){
						autoClosingAlert('#dangerMessage', 2000);
					}else{
						autoClosingAlert('#warningMessage', 2000);
					}
				}
			});
			$('#chatContent').val('');
		}
		function submitChatFunction() {
			var chatName = '<%= userID %>';
			var chatContent = $('#chatContent').val();
			
			$.ajax({
				type: "POST",
				url: "./chat2SubmitServlet",
				data: {
					chatName: encodeURIComponent(chatName),
					chatContent: encodeURIComponent(chatContent)
				},
				success: function(result) {
					if(result == 1){
						autoClosingAlert('#successMessage', 2000);
					}else if(result == 0){
						autoClosingAlert('#dangerMessage', 2000);
					}else{
						autoClosingAlert('#warningMessage', 2000);
					}
				}
			});
			$('#chatContent').val('');
		}
		function chatListFunction(type) {
			$.ajax({
				type: "POST",
				url: "./chat2ListServlet",
				data: {
					listType: type,
				},
				success: function(data) {
					if(data == "") return;
					var parsed = JSON.parse(data);
					var result = parsed.result;
					
					for(var i = 0; i < result.length; i++) {
						addChat(result[i][0].value, result[i][1].value, result[i][2].value);
					}
					lastID = Number(parsed.last);
				}
			});
		}
		function addChat(chatName, chatContent, chatTime) {
			var result = 0;
			var profile;
			var size = '<%= profileList.size() %>';
			<%
				for(int i = 0; i < profileList.size(); i++) {
					user = profileList.get(i).getUserID();
			%>
			if(chatName == '<%= user %>') {
				profile = '<%= profileList.get(i).getUserProfile() %>'
				result = result + 1 + size;
			} else {
				result = result + size;
			}
			<%
				}
			%>
			if ((result % size) == 1) {
				$('#chatList').append('<div class="row">' +
						'<div class="col-lg-12>">' +
						'<div class="media">' +
						'<a class="pull-left" href="#">' +
						'<img class="media-object img-circle" style="width: 30px; height: 30px; margin-left: 10px;" src="' + profile +'" alt="">' +
						'</a>' +
						'<div class="media-body">' +
						'<h4 class="media-heading">' +
						chatName +
						'<span class="small pull-right" style="margin-right: 10px;">' +
						chatTime +
						'</span>' +
						'</h4>' +
						'<p>' +
						chatContent +
						'</p>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<hr>');
			} else {
				$('#chatList').append('<div class="row">' +
						'<div class="col-lg-12>">' +
						'<div class="media">' +
						'<a class="pull-left" href="#">' +
						'<img class="media-object img-circle" style="width: 30px; height: 30px; margin-left: 10px;" src="images/icon.jpg" alt="">' +
						'</a>' +
						'<div class="media-body">' +
						'<h4 class="media-heading">' +
						chatName +
						'<span class="small pull-right" style="margin-right: 10px;">' +
						chatTime +
						'</span>' +
						'</h4>' +
						'<p>' +
						chatContent +
						'</p>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'</div>' +
						'<hr>');
			}
			$('#chatList').scrollTop($('#chatList')[0].scrollHeight);
		}
		function getInfiniteChat() {
			setInterval(function() {
				chatListFunction(lastID);
			}, 1000);
		}
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
				<li class="active"><a href="academy.jsp">실시간 강좌</a></li>
				<li><a href="acabbs.jsp">게시판형 강좌</a></li>
				<li><a href="acalist.jsp">강사 리스트</a></li>
				<li><a href="acatip.jsp">팁 & 프로젝트</a></li>
			</ul>
			<form method="post" action="publisher.jsp">
				<input type="submit" class="btn btn-primary pull-right" style="float: right; width: 150px; text-align: center; padding: 12px; margin-top: 12px; margin-right: 10px;" value="강의하기">
			</form>
		</div>
	</nav>
	<%
		if(userID == null){
	%>
	<div class="container-fluid bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
					width="900" height="775" id="myContent">
					<param name="movie" value="test01.swf" />
					<!--[if !IE]>-->
					<object type="application/x-shockwave-flash" data="test01.swf"
						width="900" height="775">
						<!--<![endif]-->
						<a href="http://www.adobe.com/go/getflashplayer"> <img
							src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"
							alt="Get Adobe Flash player" />
						</a>
						<!--[if !IE]>-->
					</object>
					<!--<![endif]-->
				</object>
				</object>
				<div class="portlet portlet-default pull-right" style="width: 500px; height: auto;">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green">강사에게 질문하기</i></h4>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="chat" class="panel-collapse collapse in">
						<div class="row">
							<div class="col-lg-12">
								<h3 class="text-center text-muted">질문 목록</h3>
							</div>
						</div>
						<div id="chatList" class="portlet-body chat-widget"
							style="overflow-y: auto; width: auto; height: 500px;">
						</div>
						<div class="portlet-footer">
							<div class="row">
								<div class="form-group col-xs-4">
									<input style="height: 40px;" type="text" id="chatName"
										class="form-control" placeholder="이름 (최대 8자)" maxlength="8">
								</div>
							</div>
							<div class="row" style="height: 90px;">
								<div class="form-group col-xs-10">
									<textarea style="height: 80px;" id="chatContent"
										class="form-control" placeholder="질문 내용(한 번에 최대 100자)" maxlength="100"></textarea>
								</div>
								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right"
										onclick="submitFunction();">전송</button>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%
		} else {
	%>
	<div class="container-fluid bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
					width="900" height="775" id="myContent">
					<param name="movie" value="test01.swf">
					<object type="application/x-shockwave-flash" data="test01.swf"
						width="900" height="775">
						<a href="http://www.adobe.com/go/getflashplayer"> <img
							src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"
							alt="Get Adobe Flash player">
						</a>
					</object>
				</object>
				<div class="portlet portlet-default pull-right" style="width: 500px; height: auto;">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green">강사에게 질문하기</i></h4>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="chat" class="panel-collapse collapse in">
						<div class="row">
							<div class="col-lg-12">
								<h3 class="text-center text-muted">질문 목록</h3>
							</div>
						</div>
						<div id="chatList" class="portlet-body chat-widget"
							style="overflow-y: auto; width: auto; height: 600px;">
						</div>
						<div class="portlet-footer">
							<div class="row" style="height: 90px;">
								<div class="form-group col-xs-10">
									<textarea style="height: 80px;" id="chatContent"
										class="form-control" placeholder="질문 내용(한 번에 최대 100자)" maxlength="100"></textarea>
								</div>
								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right"
										onclick="submitChatFunction();">전송</button>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%
		}
	%>
	<div class="alert alert-success" id="successMessage"
		style="display: none;">
		<strong>메세지 전송에 성공했습니다.</strong>
	</div>
	<div class="alert alert-danger" id="dangerMessage"
		style="display: none;">
		<strong>이름과 내용을 모두 입력해주세요.</strong>
	</div>
	<div class="alert alert-warning" id="warningMessage"
		style="display: none;">
		<strong>데이터베이스 오류가 발생했습니다.</strong>
	</div>
	<script type="text/javascript">
		$('#messageModal').modal("show");
	</script>
	<%
		String messageContent = null;
		if(session.getAttribute("messageContent") != null) {
			messageContent = (String)session.getAttribute("messageContent");
		}
		
		String messageType = null;
		if(session.getAttribute("messageType") != null) {
			messageType = (String)session.getAttribute("messageType");
		}
		
		if(messageContent != null) {
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div class="modal-content <% if(messageType.equals("오류 메세지")) out.println("panel-warning"); else out.println("panel-success"); %>">
					<div class="modal-header panel-heading">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times</span>
							<span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title">
							<%= messageType %>
						</h4>
					</div>
					<div class="modal-body">
						<%= messageContent %>
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
	<script type="text/javascript">
		$(document).ready(function() {
			getUnread();
			chatListFunction('0');
			getInfiniteChat();
			getInfiniteUnread();
		})
	</script>
</body>
</html>