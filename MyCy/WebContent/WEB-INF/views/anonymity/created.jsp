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
    var f = document.boardForm;

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
    
    str = f.pass.value;
    if(!str) {
        f.pass.focus();
        return false;	
    }

    var mode="${mode}";
	if(mode=="created")
		f.action="<%=cp%>/anonymity/created_ok.do";
	else if(mode=="update")
		f.action="<%=cp%>/anonymity/update_ok.do";
   	else if(mode=="reply")
   		f.action="<%=cp%>/anonymity/reply_ok.do";

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
			 
	<div class="container" role="main">
		<div class="bodyFrame col-sm-10" style="float:none; margin-left: auto; margin-right: auto;">
    
	    <div class="body-title">
	          <h3><span class="glyphicon glyphicon-book"></span> 익명 게시판 </h3>
	    </div>
	    
	    <div class="btn btn-primary btn-sm bbtn"style="background-color: #8C8C8C">
	        <i class="glyphicon glyphicon-info-sign"></i> 익명성이 보장되는 공간입니다. 자유롭게 작성하세요.
	    </div>
	    
	    <div>
	        <form name="boardForm" method="post" onsubmit="return check();">
	            <div class="bs-write">
	                <table class="table">
	                    <tbody>
	                        <tr>
	                           <td class="td1">작성자명</td>
	                           <td class="td2 col-md-5 col-sm-5">익명</td>
	                           <td class="td1" align="center">&nbsp;</td>
	                           <td class="td2 col-md-5 col-sm-5">
	                               &nbsp;
	                           </td>
	                       </tr>
	                       <tr>
	                           <td class="td1">제목</td>
	                           <td colspan="3" class="td3">
	                               <input type="text" name="title" class="form-control input-sm" value="${dto.title}" required="required">
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
	                      <tr align="left" height="40"> 
					     	 <td width="100" bgcolor="#ffffff" style="text-align:left;">패스워드</td>
					      	 <td width="500" style="padding-left:10px;"> 
					         <input type="password" name="pass" size="35" class="boxTF">
					      </td>
					  </tr>
	                   </tbody>
	                   <tfoot>
	                       <tr>
	                           <td colspan="4" style="text-align: center; padding-top: 15px;">
	                                 <c:if test="${mode=='update'}">
	                                      <input type="hidden" name="seq" value="${dto.seq}">
                                          <input type="hidden" name="page" value="${page}">
	                                      <input type="hidden" name="rows" value="${rows}">
	                                  </c:if>
	                                  <c:if test="${mode=='reply'}">
	                                         <input type="hidden" name="groupNum" value="${dto.groupNum}">
	                                         <input type="hidden" name="depth" value="${dto.depth}">
	                                         <input type="hidden" name="orderNum" value="${dto.orderNum}">
	                                         <input type="hidden" name="parent" value="${dto.seq}">
	                                         <input type="hidden" name="page" value="${page}">
	                                         <input type="hidden" name="rows" value="${rows}">
	                                  </c:if>
	                                  <button type="submit" class="btn btn-primary"> 확인 <span class="glyphicon glyphicon-ok"></span></button>
	                                  <button type="button" class="btn btn-danger" onclick="javascript:location.href='<%=cp%>/anonymity/list.do';">취소 </button>
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