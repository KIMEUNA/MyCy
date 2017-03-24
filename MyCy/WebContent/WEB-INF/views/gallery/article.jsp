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

<script type="text/javascript">
function updatePhoto(seq) {
	<c:if test="${sessionScope.member.id==dto.id}">
	     var url="<%=cp%>/gallery/update.do?seq="+seq+"&page=${page}";
	     location.href=url;
	</c:if>
}

function deletePhoto(seq) {
        if(confirm("게시물을 삭제 하시겠습니까 ?")) {
        	 var url="<%=cp%>/gallery/delete.do?seq="+seq+"&page=${page}";
        	 location.href=url;
         }		
}

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

	<div class="container" role="main">
		<div class="layoutMain">
			<div class="layoutBody">

				<div class="col-lg-12">
					<h2 class="page-header">
						<span class="glyphicon glyphicon-camera"></span> 갤러리
					</h2>
				</div>

				<div class="container">

					<!-- Portfolio Item Heading -->
					<div class="row">
						<div class="col-lg-12">
							<h3 class="page-header">
								${dto.title} <span style="float: right"> <small><span
										class="glyphicon glyphicon-user"></span>
										${dto.id}&nbsp&nbsp&nbsp&nbsp</small> <small><span
										class="glyphicon glyphicon-pencil"></span>
										${dto.regdate}&nbsp&nbsp&nbsp&nbsp</small> <small><span
										class="glyphicon glyphicon-eye-open"></span> ${dto.hit}</small>
								</span>
							</h3>
						</div>
					</div>

					<!-- /.row -->

					<!-- Portfolio Item Row -->
					<div class="row">

						<div class="col-md-8">
							<img src="<%=cp%>/img/${dto.filepath}"
								style="width: 730px; height: 480px;" border="0">
							<!-- 	<img class="img-responsive" src="http://placehold.it/730x480"
								alt=""> -->
						</div>

						<div class="col-md-4">
							<p style="font-size: 15px">${dto.content}</p>
						</div>
					</div>
					<!-- /.row -->

					<!-- Related Projects Row -->
					<div class="row">

						<div class="col-lg-12">
							<h4 class="page-header">다음 사진</h4>
						</div>

						<div class="col-sm-12 col-xs-1">
							<c:if test="${dataCount!=0 }">
								<c:forEach var="dto" items="${list}" varStatus="status">
									<c:if test="${status.index>=0 && status.index<4}">
										<img src="<%=cp%>/img/${dto.filepath}"
											onclick="javascript:article('${dto.seq}');"
											style="width: 250px; height: 160px; margin-right: 30px">
									</c:if>
								</c:forEach>
							</c:if>
						</div>

					</div>

					<!-- /.row -->
					<hr>
				</div>

				<button type="button" class="btn btn-primary"
					style="background-color: #010b26; float: right; margin-right: 20px"
					onclick="javascript:location.href='<%=cp%>/gallery/list.do?page=${page}';">
					목록으로</button>
				<c:if test="${sessionScope.member.id==dto.id}">

					<button type="button" class="btn btn-primary"
						style="background-color: #010b26; float: right; margin-right: 20px"
						onclick="deletePhoto(${dto.seq});">삭제</button>
					<button type="button" class="btn btn-primary"
						style="background-color: #010b26; float: right; margin-right: 3px"
						onclick="updatePhoto(${dto.seq});">수정</button>
				</c:if>



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