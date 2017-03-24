<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
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

<title>Justified Nav Template for Bootstrap</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<link rel="stylesheet" href="Nwagon.css" type="text/css">

<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>
<script src="Nwagon.js"></script>

<style type="text/css">
.bs-write table {
	width: 100%;
	border: 0;
	border-spacing: 0;
}

.table tbody tr td {
	border-top: none;
	font-weight: normal;
	font-family: NanumGothic, 나눔고딕, "Malgun Gothic", "맑은 고딕", 돋움, sans-serif;
}

.bs-write table td {
	padding: 3px 3px 3px 3px;
}

.bs-write .td1 {
	min-width: 100px;
	min-height: 30px;
	color: #666;
	vertical-align: middle;
}
</style>

<script type="text/javascript"
	src="<%=cp%>/res/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function check() {
	var f = document.statsForm;
	
	
}
</script>

</head>

<body>
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

	<div class="container" role="main">
		<div class="bodyFrame col-sm-10"
			style="float: none; margin-left: auto; margin-right: auto;">
			<div style="margin-top: 50px;">
				<form name="statsForm" method="post" onsubmit="return check();">
					<input type="hidden" name="seq" value="${dto.seq}">
					<div class="bs-write">
						<table class="table">
							<tbody>
								<tr>
									<td colspan="4" class="td1">
										<div class="alert alert-info">
											<h3>
												<i class="glyphicon glyphicon-info-sign"></i>
												Q.&nbsp;${dto.title}
											</h3>
										</div>
									</td>
								</tr>

								<tr align="right">
									<td colspan="4"><b>등록 시간 ${dto.regdate}</b></td>
								</tr>
								<tr align="right">
									<td colspan="4"><b>마감 시간 ${dto.enddate}</b>
									<hr></td>
								</tr>

								<tr align="left">
								<td></td>
									<td colspan="3"><b>투표자 수 : ${total } 명</b>
									<hr></td>
								</tr>

								<c:forEach var="quest" items="${quests}" varStatus="stat">
									<tr>
										<td style="width: 30%" align="right" class="td3">${stat.count}.</td>
										<td style="font-size: 20px;" align="left">&nbsp;&nbsp;
											&nbsp;&nbsp;${quest}</td>
										<td style="font-size: 20px;" align="left">&nbsp;&nbsp;
											${count[stat.index]}</td>
									</tr>
								</c:forEach>

								<tr>
									<td colspan="4"><hr style="color: red"></td>
								</tr>

							</tbody>

							<tfoot>
								<tr>
									<td colspan="4" style="text-align: center; padding-top: 15px;">
										<button type="submit" class="btn btn-primary">
											투표 완료 <span class="glyphicon glyphicon-ok"></span>
										</button>
										<button type="button" class="btn btn-danger"
											onclick="javascript:location.href='<%=cp%>/survey/list.do';">
											취소</button>
									</td>
								</tr>
							</tfoot>
						</table>
					</div>
				</form>
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