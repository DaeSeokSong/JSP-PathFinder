<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.File"%>
<%@ page import="File.FileDAO" %>
<%@ page import="File.FileDTO" %>
<%@ page import="java.util.*, java.text.*"%>
<%@ page import="java.net.*" %>
<!DOCTYPE html>
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
	%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width" , initial-scale="1">
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
		<a class="navbar-brand" href="main.jsp">For User's Compile</a>
		<div class="collapse navbar-collapse"
			id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a href="#" class="dropdown-toggle"
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
				<li><a href="box.jsp">메세지 함<span id="unread" class="label label-info"></span></a></li>
				<li><a href="main.jsp">메인</a></li>
			</ul>
		</div>
	</nav>
	<div class="container">
		<div class="col-sm-2 col-sm-push-11" style="margin: auto; text-align: center; border: 3px solid gray; border-top-style: none;
		border-right-style: none; border-bottom-style: none; padding: 10px; padding-bottom: 110px;">
			<ul id="myTab" class="nav nav-pills nav-stacked" role="tablist">
				<li role="presentation"><a href="#cloud" style="background-color: #E6E6E6; border: 1px solid #000000; margin-bottom: 10px;"
					id="cloud-tab" role="tab" data-toggle="tab" aria-controls="cloud"
					aria-expanded="false">개인 클라우드</a>
				</li>
				<li role="presentation"><a href="#filelist" style="background-color: #000000"
					id="filelist-tab" role="tab" data-toggle="tab" aria-controls="filelist"
					aria-expanded="false">프로젝트</a>
				</li>
				<li role="presentation"><a href="#freindlist" style="background-color: #000000"
					role="tab" id="freindlist-tab" data-toggle="tab"
					aria-controls="freindlist" aria-expanded="false">친구</a>
				</li>
				<li role="presentation"><a href="#Subscriptionlist" style="background-color: #000000"
					role="tab" id="Subscriptionlist-tab" data-toggle="tab"
					aria-controls="Subscriptionlist" aria-expanded="false">구독</a>
				</li>
			</ul>
		</div>
		<div class="col-sm-9 col-sm-pull-3">
			<div id="myTabContent" class="tab-content">
				<div role="tabpanel" class="tab-pane fade" id="cloud" aria-labelledby="cloud-tab">
					<form action="./UploadAction" method="post" enctype="multipart/form-data">
					<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd;">
						<tbody>
							<tr>
								<td style="width: 110px; padding-top: 14px;"><h5>파일 업로드</h5>
								<td colspan="2">
									<input type="file" name="file" class="file">
									<div class="input-group col-xs-12">
										<span class="input-group-addon"><i class="glyphicon glyphicon-file"></i></span>
										<input type="text" class="form-control input-lg" disabled placeholder="파일을 업로드하세요.">
										<span class="input-group-btn">
											<button class="browse btn btn-primary input-lg" type="button"><i class="glyphicon glyphicon-search"></i> 파일찾기</button>
										</span>
									</div>
									<input type="submit" class="btn btn-primary pull-right" value="업로드" style="margin-top: 10px;">
								</td>
							</tr>
						</tbody>
					</table>
					</form>
					<table class="table table-bordered table-hover" style="text-align: center; border: 1px solid #dddddd;">
						<tbody>
							<tr>
								<td style="width: 110px;"><h5>새폴더</h5>
								<td colspan="2">
									<input type="submit" class="btn btn-primary" value="새폴더" style="margin: 0 auto;" onclick="window.open('mkdir.jsp', 'FUCloud', 'width=500px, height=80px')"><br>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
					<%
						request.setCharacterEncoding("UTF-8");
						String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
						String nowdir = (String) request.getAttribute("name");
						String content = (String) request.getAttribute("content");

						if (content != null) {
							savePath = content;
						}

						if (nowdir == null) nowdir = "FILE";

						ArrayList<FileDTO> fileList = new FileDAO().getList(nowdir);
						String files[] = new File(savePath).list();
						for (String file : files) {
							if (file.contains(".") == false) {
								out.write("<a href=\"" + request.getContextPath() + "/DownloadAction?file="
										+ URLEncoder.encode(file, "UTF-8") + "\">" + file + "</a><br>");
							} else continue;
						}
						for (FileDTO file : fileList) {
							out.write("<a href=\"" + request.getContextPath() + "/DownloadAction?file="
									+ URLEncoder.encode(file.getFileRealName(), "UTF-8") + "\">" + file.getFileRealName()
									+ "(다운로드 횟수: " + file.getDownloadCount() + ")</a><br>");
						}
					%>
				</div>
				<div role="tabpanel" class="tab-pane fade" id="filelist" aria-labelledby="filelist-tab">
					<p>프로젝트 목록</p>
				</div>
				<div role="tabpanel" class="tab-pane fade" id="freindlist" aria-labelledby="freindlist-tab">
					<p>친구 목록</p>
				</div>
				<div role="tabpanel" class="tab-pane fade" id="Subscriptionlist" aria-labelledby="Subscriptionlist-tab">
					<p>구독한 사람 목록</p>
				</div>
			</div>
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