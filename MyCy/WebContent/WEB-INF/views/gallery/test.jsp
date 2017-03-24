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
<link href="<%=cp%>/res/css/layout/shop-homepage.css" rel="stylesheet">

<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

<script type="text/javascript">
	function article(seq) {
		var url = "${articleUrl}&seq=" + seq;
		location.href = url;
	}
</script>

</head>

<body>
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<!-- Page Content -->
	<div class="container">
		<div class="col-lg-12">
			<h2 class="page-header">
				<span class="glyphicon glyphicon-camera"></span> 갤러리
			</h2>
		</div>

		<div class="row">
	
			<div class="col-md-12" style="margin-top: 10px" >
				<div style="clear: both; height: 30px; line-height: 30px;">
					<div style="float: left;">${dataCount}개(${page}/${total_page}
						페이지)</div>
					<div style="float: right;">&nbsp;</div>
				</div>
				<div class="row carousel-holder">

					<div class="col-md-12" style="margin: 0 auto">
						<div id="carousel-example-generic" class="carousel slide"
							data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#carousel-example-generic" data-slide-to="0"
									class="active"></li>
								<li data-target="#carousel-example-generic" data-slide-to="1"></li>
								<li data-target="#carousel-example-generic" data-slide-to="2"></li>
							</ol>
							<div class="carousel-inner">
								<div class="item active">
									<img src="<%=cp%>/img/${hit_dto.filepath}"
										onclick="javascript:article('${hit_dto.seq}');"
										style="width: 1150px; height: 330px;" border="0">
									<h6 class="pull-right"
										style="font-size: 13px; margin-right: 30px">
										No.&nbsp;${hit_dto.seq}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										제목:&nbsp;${hit_dto.title}&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; <span
											class="glyphicon glyphicon-user"></span>&nbsp;${hit_dto.id}&nbsp;&nbsp;
										<span class="glyphicon glyphicon-pencil"></span>&nbsp;${hit_dto.regdate}&nbsp;&nbsp;
										<span class="glyphicon glyphicon-eye-open"></span>&nbsp;${hit_dto.hit}
									</h6>
								</div>
								<!-- 			<div class="item">
									<img class="slide-image" src="http://placehold.it/850x300"
										alt="">
								</div>
								<div class="item">
									<img class="slide-image" src="http://placehold.it/850x300"
										alt="">
								</div> -->
							</div>
							<a class="left carousel-control" href="#carousel-example-generic"
								data-slide="prev"> <span
								class="glyphicon glyphicon-chevron-left"></span>
							</a> <a class="right carousel-control"
								href="#carousel-example-generic" data-slide="next"> <span
								class="glyphicon glyphicon-chevron-right"></span>
							</a>
						</div>
					</div>

				</div>

				<div class="row">
					<c:if test="${dataCount!=0 }">
						<c:forEach var="dto" items="${list}" varStatus="status">
							<div class="col-sm-4 col-lg-4 col-md-4">
								<div class="thumbnail">
									<img src="<%=cp%>/img/${dto.filepath}"
										style="width: 320px; height: 230px;" border="0">
									<div class="caption">
										<h4 class="pull-right" style="font-size: 13px">${dto.regdate}</h4>

										<span class="title"
											onclick="javascript:article('${dto.seq}');">
											${dto.title} </span>
										<%-- <h4 >
									<a href="#">${dto.title}</a>
								</h4> --%>
										<p>${dto.id}</p>
										<p class="pull-right">No.&nbsp;${dto.seq}</p>
										<p>
											<span class="glyphicon glyphicon-eye-open">&nbsp;${dto.hit}</span>
										</p>
									</div>
								</div>
							</div>
						</c:forEach>

						<c:set var="n" value="${list.size()}" />
						<c:if test="${n > 0 && n % 3 != 0}">
							<c:forEach var="i" begin="${n%3+1}" end="3" step="1">
								<div class="imgLayout">&nbsp;</div>
							</c:forEach>
						</c:if>

						<c:if test="${n!=0 }">
							<c:out value="</div>" escapeXml="false" />
						</c:if>
					</c:if>

					<div class="paging"
						style="text-align: center; min-height: 50px; line-height: 50px;">
						<c:if test="${dataCount==0 }">
	                			  등록된 게시물이 없습니다.
	          				</c:if>
						<div align="right">
							<a class="btn btn-primary" style="background-color: #010b26"
								href="<%=cp%>/gallery/created.do">사진 등록</a>
						</div>
						<c:if test="${dataCount!=0 }">
	              			  ${paging}
	           				</c:if>
					</div>

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
	<script src="<%=cp%>/res/jquery/js/jquery.js"></script>
	<script src="<%=cp%>/res/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>