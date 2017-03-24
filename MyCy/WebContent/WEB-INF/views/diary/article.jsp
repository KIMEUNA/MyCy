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

<title>상단탭 이름 설정하세요</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript">
function deleteDiary() {
<c:if test="${sessionScope.member.id=='admin' || sessionScope.member.id==dto.id}">
    var seq = "${dto.seq}";
    var page = "${page}";
    var params = "seq="+seq+"&page="+page;
    var url = "<%=cp%>/diary/delete.do?" + params;

    if(confirm("위 자료를 삭제 하시 겠습니까 ? "))
    	location.href=url;
</c:if>    
<c:if test="${sessionScope.member.id!='admin' && sessionScope.member.id!=dto.id}">
    alert("게시물을 삭제할 수 없습니다.");
</c:if>
}

function updateDiary() {
<c:if test="${sessionScope.member.id==dto.id}">
    var seq = "${dto.seq}";
    var page = "${page}";
    var params = "seq="+seq+"&page="+page;
    var url = "<%=cp%>/diary/update.do?" + params;

    location.href=url;
</c:if>

<c:if test="${sessionScope.member.id!=dto.id}">
   alert("게시물을 수정할 수  없습니다.");
</c:if>
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
				<div class="bbs-article">
					<table class="table">
						<thead>
							<tr>
								<th colspan="2" style="text-align: center;"><c:if
										test="${dto.depth!=0}">
	                                     [Re]
	                                 </c:if> ${dto.title}</th>
							</tr>
						<thead>
						<tbody>
							<tr>
								<td style="text-align: left;">이름 : ${dto.id}</td>
								<td style="text-align: right;">${dto.regdate} <i></i>조회 :
									${dto.hit}
								</td>
							</tr>
							<tr>
								<td colspan="2" style="height: 230px;">${dto.content}</td>
							</tr>
							<tr>
								<td colspan="2"><span
									style="display: inline-block; min-width: 45px;">이전글</span> : <c:if
										test="${not empty preReadDto }">
										<a
											href="<%=cp%>/diary/article.do?${params}&seq=${preReadDto.seq}">${preReadDto.title}</a>
									</c:if></td>
							</tr>
							<tr>
								<td colspan="2" style="border-bottom: #d5d5d5 solid 1px;">
									<span style="display: inline-block; min-width: 45px;">다음글</span>
									: <c:if test="${not empty nextReadDto }">
										<a
											href="<%=cp%>/diary/article.do?${params}&seq=${nextReadDto.seq}">${nextReadDto.title}</a>
									</c:if>
								</td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td>
									<button type="button" class="btn btn-default btn-sm wbtn"
										onclick="javascript:location.href='<%=cp%>/diary/reply.do?seq=${dto.seq}&page=${page}';">답변</button>
									<c:if test="${sessionScope.member.id==dto.id}">
										<button type="button" class="btn btn-default btn-sm wbtn"
											onclick="updateDiary();">수정</button>
									</c:if> <c:if
										test="${sessionScope.member.id==dto.id || sessionScope.member.id=='admin'}">
										<button type="button" class="btn btn-default btn-sm wbtn"
											onclick="deleteDiary();">삭제</button>
									</c:if>
								</td>
								<td align="right">
									<button type="button" class="btn btn-default btn-sm wbtn"
										onclick="javascript:location.href='<%=cp%>/diary/list.do?${params}';">
										목록으로</button>
								</td>
							</tr>
						</tfoot>
					</table>
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

</body>
</html>