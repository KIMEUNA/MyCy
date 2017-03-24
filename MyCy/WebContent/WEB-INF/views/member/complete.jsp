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

<title>우성오빵 ~~</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
   rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

</head>

<body>
   <div>
      <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
   </div>

<div class="container">
   <div class="jumbotron">
        <h1>${title}</h1>
    </div>
    <div class="bodyFrame">
       <p class="lead text-center">${message}</p>
       <p style="max-width: 400px; margin: 0 auto;">
          <button type="button" class="btn btn-primary" style="background-color:#010b26" onclick="javascript:location.href='<%=cp%>/member/login.do';"> 로그인 &raquo; </button>
       </p>
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