<%@page import="java.text.SimpleDateFormat"%>
<%@page import="javax.print.attribute.standard.DateTimeAtCompleted"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String cp = request.getContextPath();

	Date now = new Date();
	Date date = new Date();
	
	date.setDate(date.getDate()+1);
	
	int item = 2;

	String itemStr = request.getParameter("item");
	if (itemStr != null)
		item = Integer.parseInt(itemStr);
%>
<!DOCTYPE html>
<html>
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
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

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
       var f = document.surveyForm;
       
        f.action="<%=cp%>/survey/created_ok.do";

		return true;
	}
	function dateChk() {
		var f = document.surveyForm;
		var date = f.enddate.value;
		
		alert(date);
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

				<div class="body-title">
					<h3>
						<span class="glyphicon glyphicon-book"></span> 투표 등록하기
					</h3>
				</div>

				<div class="alert alert-info">
					<i class="glyphicon glyphicon-info-sign"></i>
				</div>

				<div>
					<form name="surveyForm" method="post" onsubmit="return check();">
						<div class="bs-write">
							<table class="table">
								<tbody>
									<tr>
										<td class="td1">작성자명</td>
										<td class="td2 col-md-5 col-sm-5">
											<p class="form-control-static">${sessionScope.member.name}</p>
											<!-- ${sessionScope.member.name} : 로그인된 사용자 정보 저장 -->
										</td>
										<td class="td1" align="center">&nbsp;</td>
										<td class="td2 col-md-5 col-sm-5">&nbsp;</td>
									</tr>
									<tr>
										<td class="td1"></td>
										<td colspan="3" class="td3"><input style="width: 30%"
											placeholder="제목" type="text" name="title"
											class="form-control input-sm" required="required"></td>
									</tr>
									<tr>
										<td colspan="4"><hr style="color: #fff"></td>
									</tr>
									<c:forEach begin="1" end="<%=item%>" varStatus="stat">
										<tr>
											<td class="td1"></td>
											<td colspan="2" class="td4"><input placeholder="항목 입력"
												type="text" name="quest" class="form-control input-sm"
												required="required"></td>
											<td class="td4"><c:if test="${stat.count > 2}">
													<button style="width: 30px;" type="button"
														class="btn btn-danger"
														onclick="javascript:location.href='<%=cp%>/survey/created.do?item=<%=item - 1%>'">
														<!-- <span class="glyphicon glyphicon-ok"> </span> -->
													</button>
												</c:if>
											</td>
										</tr>
									</c:forEach>
									
									<c:if test="<%=item < 5%>">
										<tr>
											<td class="td1"></td>
											<td colspan="3" class="td4">
												<input style="width: 30%"
													type="button" value="항목 추가" class="form-control input-sm"
													onclick="javascript:location.href='<%=cp%>/survey/created.do?item=<%=item + 1%>'">
											</td>
										</tr>
									</c:if>
									
									<tr>
										<td colspan="4"><hr color="red"></td>
									</tr>

									<tr>
										<td class="td1">마감시간</td>
										<td colspan="3" class="td3">
											<input style="width: 25%" class="form-control input-sm"
												type="datetime-local" name="enddate" 
												value="<%=new SimpleDateFormat("yyyy-MM-dd").format(date) %>T12:00" 
												required="required" 
												onchange="return dateChk()">
										</td>
									</tr>
									<tr>
										<td colspan="4"><hr style="color: #fff"></td>
									</tr>
									<tr>
										<td class="td1">복수 선택</td>
										<td class="td3">
											<input style="width: 10%; border: none"
												type="checkbox" name="choice"
												class="form-control input-sm" >
										</td>
										<td colspan="2"></td>
									</tr>
								</tbody>

								<tfoot>
									<tr>
										<td colspan="4" style="text-align: center; padding-top: 15px;">
											<button type="submit" class="btn btn-primary">
												투표 등록 <span class="glyphicon glyphicon-ok"></span>
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