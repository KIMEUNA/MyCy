<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">			<!-- 부트스트랩 -->
<meta name="viewport" content="width=device-width, initial-scale=1">	<!-- 부트스트랩 -->
<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>spring</title>

<link rel="stylesheet" href="<%=cp%>/res/jquery/css/smoothness/jquery-ui.min.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/res/bootstrap/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/res/bootstrap/css/bootstrap-theme.min.css" type="text/css"/>

<link rel="stylesheet" href="<%=cp%>/res/css/style.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/res/css/layout/layout.css" type="text/css"/>
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">

<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>
<script type="text/javascript" src="<%=cp%>/res/jquery/js/jquery-1.12.4.min.js"></script>
<style type="text/css">
.form-signin {
  max-width: 400px;
  padding: 15px;
  margin: 0 auto;
}

@media (min-width: 768px) {
  .form-signin {
    padding-top: 70px;
  }
}

.form-signin-heading {
  text-align: center;
  font-weight:bold;  
  font-family: NanumGothic, 나눔고딕, "Malgun Gothic", "맑은 고딕", sans-serif;
  margin-bottom: 30px;
}

.lbl {
   position:absolute; 
   margin-left:15px; margin-top: 13px;
   color: #999999;
   font-family: NanumGothic, 나눔고딕, "Malgun Gothic", "맑은 고딕", 돋움, sans-serif;
}

.loginTF {
  max-width: 370px; height:45px;
  padding: 5px;
  padding-left: 15px;
  margin-top:5px; margin-bottom:15px;
}
</style>

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

        str = f.pass.value;
        if(!str) {
            f.pass.focus();
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

<div class="container" role="main">

    <div class="bodyFrame">
    <form class="form-signin" name="loginForm" method="post">
        <h2 class="form-signin-heading">Log In</h2>
        <label for="id" id="lblId" class="lbl">아이디</label>
        <input type="text" id="id" name="id" class="form-control loginTF" autofocus="autofocus"
                  onfocus="document.getElementById('lblId').style.display='none';"
	              onblur="bgLabel(this, 'lblId');">
        <label for="pass" id="lblPass" class="lbl">패스워드</label>
        <input type="password" id="pass" name="pass" class="form-control loginTF"
                  onfocus="document.getElementById('lblPass').style.display='none';"
	              onblur="bgLabel(this, 'lblPass');">
        <button style="background-color:#010b26" class="btn btn-primary" type="button" onclick="sendLogin();">로그인 </button>
        
        <div style="margin-top:10px; text-align: center;">
            <button type="button" class="btn btn-link" onclick="location.href='<%=cp%>/member/member.do';">회원가입</button>
            <button type="button" class="btn btn-link">아이디찾기</button>
            <button type="button" class="btn btn-link">패스워드찾기</button>
        </div>
        
        <div style="margin-top:10px; text-align: center;">${message}</div>
        
    </form>
    </div>
</div>

<div>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<script src="<%=cp%>/res/js/ie10-viewport-bug-workaround.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=cp%>/res/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="<%=cp%>/res/jquery/js/jquery.ui.datepicker-ko.js"></script>
<script type="text/javascript" src="<%=cp%>/res/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>