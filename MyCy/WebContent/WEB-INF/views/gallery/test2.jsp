<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");

	String cp = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>갤러리</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<link href="<%=cp%>/res/css/layout/portfolio-item.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

</head>

<body>
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="container" role="main">
		<div class="layoutMain">
			<div class="layoutBody">

				<div class="col-lg-12">
					<h2 class="page-header">
						<span class="glyphicon glyphicon-picture"></span> 갤러리
					</h2>
				</div>

				<div class="container">

					<!-- Portfolio Item Heading -->
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								${dto.title} 
								<span style="float: right">								
								<small><span class="glyphicon glyphicon-user"></span>
								${sessionScope.member.id}&nbsp&nbsp&nbsp&nbsp</small>
								<small><span class="glyphicon glyphicon-pencil"></span>
								${dto.regdate}&nbsp&nbsp&nbsp&nbsp</small>
								<small><span class="glyphicon glyphicon-eye-open"></span>
								
								${dto.hit}</small>
								</span>
							</h3>
						</div>
					</div>

					<!-- /.row -->

					<!-- Portfolio Item Row -->
					<div class="row">

						<div class="col-md-8">
						 <img src="<%=cp%>/uploads/gallery/${dto.filepath}" 
						 style="width: 730px; height: 480px;" border="0">
						<!-- 	<img class="img-responsive" src="http://placehold.it/730x480"
								alt=""> -->
						</div>

						<div class="col-md-4">
							<p style="font-size: 15px">
							${dto.content}</p>
						</div>

					</div>
					<!-- /.row -->

					<!-- Related Projects Row -->
					<div class="row">

						<div class="col-lg-12">
							<h4 class="page-header">다음 사진</h4>
						</div>

						<div class="col-sm-3 col-xs-6">
							<a href="#"> <img class="img-responsive portfolio-item"
								src="http://placehold.it/500x300" alt="">
							</a>
						</div>

						<div class="col-sm-3 col-xs-6">
							<a href="#"> <img class="img-responsive portfolio-item"
								src="http://placehold.it/500x300" alt="">
							</a>
						</div>

						<div class="col-sm-3 col-xs-6">
							<a href="#"> <img class="img-responsive portfolio-item"
								src="http://placehold.it/500x300" alt="">
							</a>
						</div>

						<div class="col-sm-3 col-xs-6">
							<a href="#"> <img class="img-responsive portfolio-item"
								src="http://placehold.it/500x300" alt="">
							</a>
						</div>

					</div>
					<!-- /.row -->
					<hr>
				</div>




			</div>
		</div>
	</div>
	<div>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>

	<script src="<%=cp%>/res/js/ie10-viewport-bug-workaround.js"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="<%=cp%>/res/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=cp%>/res/jquery/js/jquery.js"></script>

</body>
</html>