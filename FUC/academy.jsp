<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<div style="text-align: center; color: red;"><h2>HOT Class</h2></div>
	<div class="container">
		<div id="myCarousel" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px; margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C언어</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>Fucademy !</p></div>
						<a href="academyEx1.jsp" class="btn btn-primary pull-right" style="margin-right: 33px;">수강하기</a>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C언어</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C언어</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel2" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C++</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						<a href="academyEx2.jsp" class="btn btn-primary pull-right" style="margin-right: 33px;">수강하기</a>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C++</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C++</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel2" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel2" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel3" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C#</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C#</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>C#</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel3" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel3" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel4" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Java</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Java</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Java</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel4" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel4" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div class="container">
			<div id="myCarousel5" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 45px; float: right;">
				<div class="carousel-inner" style="float: right;">
					<div class="item active">
						<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>Python</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
					<div class="item">
						<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>Python</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
					<div class="item">
						<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>Python</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
				</div>
				<a class="left carousel-control" href="#myCarousel5" data-slide="prev">
					<span class="glyphicon glyphicon-chevron-left"></span>
				</a>
				<a class="right carousel-control" href="#myCarousel5" data-slide="next">
					<span class="glyphicon glyphicon-chevron-right"></span>
				</a>
			</div>
			<div id="myCarousel6" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-left: 5px; margin-right: 15px; float: left;">
				<div class="carousel-inner" style="float: right;">
					<div class="item active">
						<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>JavaSrcipt</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
					<div class="item">
						<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>JavaSrcipt</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
					<div class="item">
						<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
						<div class="carousel-caption d-none d-md-block">
							<h3>JavaSrcipt</h3>
							<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
						</div>
					</div>
				</div>
				<a class="left carousel-control" href="#myCarousel6" data-slide="prev">
					<span class="glyphicon glyphicon-chevron-left"></span>
				</a>
				<a class="right carousel-control" href="#myCarousel6" data-slide="next">
					<span class="glyphicon glyphicon-chevron-right"></span>
				</a>
			</div>
		</div>
	</div>
	<div class="container">
		<div id="myCarousel7" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Css</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Css</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Css</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel7" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel7" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel8" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Html</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Html</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Html</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel8" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel8" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel9" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Php</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Php</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>Php</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel9" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel9" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
		<div id="myCarousel10" class="carousel slide" data-ride="carousel" style="width: 250px; height: 150px; margin-top: 15px;  margin-right: 30px; float: right;">
			<div class="carousel-inner" style="float: right;">
				<div class="item active">
					<img src="images/1.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>기타</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/2.jpg" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>기타</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
				<div class="item">
					<img src="images/3.png" style="width: 250px; height: 150px; margin-left: auto; margin-right: auto; display: block;">
					<div class="carousel-caption d-none d-md-block">
						<h3>기타</h3>
						<div class="container-fulid" style="color: black; background-color: white;"><p>강의 제목</p></div>
					</div>
				</div>
			</div>
			<a class="left carousel-control" href="#myCarousel10" data-slide="prev">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</a>
			<a class="right carousel-control" href="#myCarousel10" data-slide="next">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</a>
		</div>
	</div>
	<hr>
	<div style="text-align: center;"><h2>수업 찾기</h2></div>
	<div style="margin-left: 500px;">
		<!-- 검색창 부분 jsp 파일 만들어야 함 -->
		<form class="form-inline my-2 my-lg-0">
      		<input class="form-control mr-sm-2" style="width: 480px;" type="search" placeholder="Search" aria-label="Search">
      		<button class="btn btn-outline-success my-2 my-sm-0" type="submit">검색</button>
    	</form>
    </div>
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