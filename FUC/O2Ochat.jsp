<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="user.UserDAO" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		
		String toID = null;
		if(request.getParameter("toID") != null) {
			toID= (String)request.getParameter("toID");
		}
		
		if(userID == null) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "로그인이 필요합니다.");
			response.sendRedirect("login.jsp");
			return;
		}
		if(toID == null) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "대화 상대가 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		if(userID.equals(URLDecoder.decode(toID, "UTF-8"))) {
			session.setAttribute("messageType", "오류 메세지");
			session.setAttribute("messageContent", "자기 자신한테는 1대1 대화를 할 수 없습니다.");
			response.sendRedirect("main.jsp");
			return;
		}
		
		String fromProfile = new UserDAO().getProfile(userID);
		String toProfile = new UserDAO().getProfile(toID);
	%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
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
	function RsubmitFunction() {
		var fromID = '<%= userID %>';
		var toID = '<%= toID %>';
		var chatContent = $('#chatContent').val();
		
		$.ajax({
			type: "POST",
			url: "./rchatSubmitServlet",
			data: {
				fromID: encodeURIComponent(fromID),
				toID: encodeURIComponent(toID),
				chatContent: encodeURIComponent(chatContent),
			},
			success: function(result) {
				if(result == 1) {
					autoClosingAlert('#successMessage', 2000);
				}else if(result == 0) {
					autoClosingAlert('#dangerMessage', 2000);
				}else {
					autoClosingAlert('#warningMessage', 2000);
				}
			}
		});
		$('#chatContent').val('');
	}
	function chatListFunction(type) {
		var fromID = '<%= userID %>';
		var toID = '<%= toID %>';
		
		$.ajax({
			type: "POST",
			url: "./rgchatListServlet",
			data: {
				fromID: encodeURIComponent(fromID),
				toID: encodeURIComponent(toID),
				listType: type
			},
			success: function(data) {
				if(data == "") return;
				
				var parsed = JSON.parse(data);
				var result = parsed.result;
				
				for(var i = 0; i < result.length; i++) {
					if(result[i][0].value == fromID) {
						result[i][0].value = '나';
					}
					addChat(result[i][0].value, result[i][2].value, result[i][3].value);
				}
				lastID = Number(parsed.last);
			}
		});
	}
	function addChat(chatName, chatContent, chatTime) {
		if(chatName == '나') {
		$('#chatList').append('<div class="row">' +
				'<div class="col-lg-12>">' +
				'<div class="media">' +
				'<a class="pull-left" href="#">' +
				'<img class="media-object img-circle" style="width: 30px; height: 30px; margin-left: 10px;" src="<%= fromProfile %>" alt="">' +
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
					'<img class="media-object img-circle" style="width: 30px; height: 30px; margin-left: 10px;" src="<%= toProfile %>" alt="">' +
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

	<div class="container bootstrap snippet">
		<div class="row">
			<div class="col-xs-12">
				<div class="portlet portlet-default" style="width: 400px; height: auto;">
					<div class="portlet-heading">
						<div class="portlet-title">
							<h4><i class="fa fa-circle text-green">실시간 채팅창</i></h4>
						</div>
						<div class="clearfix"></div>
					</div>
					<div id="chat" class="panel-collapse collapse in">
						<div class="row">
							<div class="col-lg-12">
								<h3 class="text-center text-muted"><%= toID %></h3>
							</div>
						</div>
						<div id="chatList" class="portlet-body chat-widget"
							style="overflow-y: auto; width: auto; height: 450px;">
						</div>
						<div class="portlet-footer">
							<div class="row" style="height: 90px;">
								<div class="form-group col-xs-10">
									<textarea style="height: 80px;" id="chatContent"
										class="form-control" placeholder="내용" maxlength="100"></textarea>
								</div>
								<div class="form-group col-xs-2">
									<button type="button" class="btn btn-default pull-right"
										onclick="RsubmitFunction();">전송</button>
									<div class="clearfix"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
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