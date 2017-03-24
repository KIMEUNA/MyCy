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

<title>Justified Nav Template for Bootstrap</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
   rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

<script type="text/javascript">
function bgLabel(ob, id) {
	    if(!ob.value) {
		    document.getElementById(id).style.display="";
	    } else {
		    document.getElementById(id).style.display="none";
	    }
}

function sendLogin() {
        var f = document.loginForm;

    	var str = f.id.value;
        if(!str) {
            f.id.focus();
            return false;
        }

        str = f.userPwd.value;
        if(!str) {
            f.userPwd.focus();
            return false;
        }

        f.action = "<%=cp%>/member/login_ok.do";
        f.submit();
}
</script>

</head>

<body>
   <div>
      <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
   </div>

<div class="container">
 
     <div class="bodyFrame">
    <form class="form-signin" name="loginForm" method="post">
        <h2 class="form-signin-heading">Log In</h2>
        <label for="id" id="lblId" class="lbl">아이디</label>
        <input type="text" id="id" name="id" class="form-control loginTF" autofocus="autofocus"
                  onfocus="document.getElementById('lblId').style.display='none';"
	              onblur="bgLabel(this, 'lblId');">
        <label for="userPwd" id="lblUserPwd" class="lbl">패스워드</label>
        <input type="password" id="userPwd" name="userPwd" class="form-control loginTF"
                  onfocus="document.getElementById('lblUserPwd').style.display='none';"
	              onblur="bgLabel(this, 'lblUserPwd');">
        <button style="background-color:#010b26" class="btn btn-primary" type="button" onclick="sendLogin();">로그인 </button>
        
        <div style="margin-top:10px; text-align: center;">
            <button type="button" class="btn btn-link" onclick="location.href='<%=cp%>/member/member.do';">회원가입</button>
            <button type="button" class="btn btn-link" onclick="#">아이디찾기</button>
            <button type="button" class="btn btn-link" onclick="#">패스워드찾기</button>
        </div>
        
        <div style="margin-top:10px; text-align: center;">${message}</div>
        
    </form>
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