<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
   request.setCharacterEncoding("utf-8");

   String cp = request.getContextPath();
%>
<div class="container">
   <div class="header">
      <h3 class="text-muted">MyBlog</h3>
      <nav>
         <ul class="nav nav-pills pull-right" >
            <c:if test="${empty sessionScope.member}">
               <a href="<%=cp%>/member/login.do"><span class="glyphicon glyphicon-log-in"></span> 로그인</a> <i></i>
                   <a href="<%=cp%>/member/member.do"><span class="glyphicon glyphicon-user"></span> 회원가입</a>
            </c:if>
      
      <c:if test="${not empty sessionScope.member}">
         <span style="color: blue;">${sessionScope.member.name}</span>님 <i></i>        
         <a href="<%=cp%>/member/logout.do"><span
            class="glyphicon glyphicon-log-out"></span> 로그아웃</a>
      </c:if>
         </ul>
      </nav>
   </div>
</div>
<div class="container">
   <div class="masthead">
      <nav>
         <ul class="nav nav-justified">
            <li><a href="<%=cp%>/">Home</a></li>
            <li><a href="<%=cp%>/diary/list.do">다이어리</a></li>
            <li><a href="<%=cp%>/gallery/list.do">갤러리</a></li>
            <li><a href="<%=cp%>/anonymity/list.do">익명게시판</a></li>
            <li><a href="<%=cp%>/survey/list.do">투표게시판</a></li>
            <li><a href="<%=cp%>/visitor/list.do">방명록</a></li>
         </ul>
      </nav>
   </div>
</div>