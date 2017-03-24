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

<title>수정</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
   rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

<script type="text/javascript">
function check() {
    var f = document.boardForm;

	var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/visitor/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/visitor/update.do";

		// image 버튼, submit은 submit() 메소드 호출하면 두번전송
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
         
         

	    <div>
	        <form name="boardForm" method="post" onsubmit="return check();">
	            <div class="bs-write">
	                <table class="table">
	                    <tbody>
	                        <tr>
	                            <td class="td1">작성자명</td>
	                            <td class="td2 col-md-5 col-sm-5">
	                                <p class="form-control-static">${dto.id}</p>
	                                <!-- ${sessionScope.member.userName} : 로그인된 사용자 정보 저장 -->
	                            </td>
	                            <td class="td1" align="center">&nbsp;</td>
	                            <td class="td2 col-md-5 col-sm-5">
	                                &nbsp;
	                            </td>
	                        </tr>

	                        <tr>
	                            <td class="td1" colspan="4" style="padding-bottom: 0px;">내용</td>
	                        </tr>
	                        <tr>
	                            <td colspan="4" class="td4">
	                            	<textarea name="content" class="form-control" rows="13" required="required">${dto.content}</textarea>
	                            </td>
	                        </tr>
	                    </tbody>
	                    <tfoot>
	                        <tr>
	                            <td colspan="4" style="text-align: center; padding-top: 15px;">
	                            	<c:if test="${mode=='update'}">
	                            		<input type="hidden" name="page" value="${pageNum}">
	                            		<input type="hidden" name="seq" value="${dto.seq}">
	                            	</c:if>
	                                  <button type="submit" class="btn btn-primary"> 확인 <span class="glyphicon glyphicon-ok"></span></button>
	                                  <button type="button" class="btn btn-danger" onclick="javascript:location.href='<%=cp%>/visitor/list.do';"> 취소 </button>
	                            </td>
	                        </tr>
	                    </tfoot>
	                </table>
	            </div>
	        </form>
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