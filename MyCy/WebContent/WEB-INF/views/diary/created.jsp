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
  function check() {
        var f = document.diaryForm;

    	var str = f.title.value;
        if(!str) {
            f.title.focus();
            return false;
        }

    	str = f.content.value;
        if(!str) {
            f.content.focus();
            return false;
        }

    	var mode="${mode}";
    	if(mode=="regdate")
    		f.action="<%=cp%>/diary/regdate_ok.do";
   		else 	if(mode=="reply")
       		f.action="<%=cp%>/diary/reply_ok.do";
    	else if(mode=="update")
    		f.action="<%=cp%>/diary/update_ok.do";

    	// <input type='submit' ..>,  <input type='image' ..>, <button>은 submit() 메소드 호출하면 두번전송
        return true;
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

				<form name="diaryForm" method="post" onsubmit="return check();">
					<div class="bs-write">
						<table class="table">
							<tbody>
								<tr>
									<td class="td1">작성자명</td>
									<td class="td2 col-md-5 col-sm-5">
										<p class="form-control-static">${sessionScope.member.id}</p>
									</td>
									<td class="td1" align="center">&nbsp;</td>
									<td class="td2 col-md-5 col-sm-5">&nbsp;</td>
								</tr>
								<tr>
									<td class="td1">제목</td>
									<td colspan="3" class="td3"><input type="text"
										name="title" class="form-control input-sm"
										value="${dto.title}" required="required"></td>
								</tr>
								<tr>
									<td class="td1" colspan="4" style="padding-bottom: 0px;">내용</td>
								</tr>
								<tr>
									<td colspan="4" class="td4"><textarea name="content"
											class="form-control" rows="13" required="required">${dto.content}</textarea>
									</td>
								</tr>
							</tbody>
							<tfoot>
								<tr>
									<td colspan="4" style="text-align: center; padding-top: 15px;">
										<button type="submit" class="btn btn-primary">
											확인 <span class="glyphicon glyphicon-ok"></span>
										</button>
										<button type="button" class="btn btn-danger"
											onclick="javascript:location.href='<%=cp%>/diary/list.do';">
											취소</button> <c:if test="${mode=='reply'}">
											<input type="hidden" name="page" value="${page}">
											<input type="hidden" name="groupNum" value="${dto.groupNum}">
											<input type="hidden" name="orderNum" value="${dto.orderNum}">
											<input type="hidden" name="depth" value="${dto.depth}">
											<input type="hidden" name="seq" value="${dto.seq}">
										</c:if> <c:if test="${mode=='update'}">
											<input type="hidden" name="page" value="${page}">
											<input type="hidden" name="seq" value="${dto.seq}">
										</c:if>

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