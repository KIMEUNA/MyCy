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

function selectList() {
	var f = document.selectListForm;
}
</script>

</head>

<body>
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>

		
<div class="container" role="main">
    <div class="bodyFrame col-sm-10" style="float:none; margin-left: auto; margin-right: auto;">
        
	    <div class="body-title">
	          <h3><span class="glyphicon glyphicon-book"></span> 익명 게시판 </h3>
	    </div>
	    
	    <div class="btn btn-primary btn-sm bbtn"style="background-color: #8C8C8C">
	        <i class="glyphicon glyphicon-info-sign"></i> 익명성 보장 자유롭게 작성하는 게시판~
	    </div>
	
	    <div>
	        <div style="clear: both; height: 35px; line-height: 35px;">
	            <div style="float: right;">
	                <form name="selectListForm" action="<%=cp%>/anonymity/list.do" method="post">
                       <input type="hidden" name="searchKey" value="${searchKey}">
                       <input type="hidden" name="searchValue" value="${searchValue}">
	                </form>
	            </div>
	        </div>
	        
	        <div class="table-responsive" style="clear: both;"> <!-- 테이블 반응형 -->
	            <table class="table table-hover">
	                <thead>
	                    <tr>
	                        <th class="text-center" style="width: 70px;">번호</th>
	                        <th >제목</th>
	                        <th class="text-center" style="width: 100px;">작성자</th>
	                        <th class="text-center" style="width: 100px;">날짜</th>
	                        <th class="text-center" style="width: 70px;">조회수</th> 
	                    </tr>
	                </thead>
	                <tbody>
	                <c:forEach var="dto" items="${list}">
	                    <tr>
	                        <td class="text-center">${dto.listNum}</td>
	                        <td>
	                            <c:if test="${dto.depth>0}">
	                             <c:forEach var="i" begin="1" end="${dto.depth}">
	                                &nbsp;&nbsp;
	                             </c:forEach>
	                        	 <img src="<%=cp%>/res/images/re.gif">
	                            </c:if>
	                             <a href="${articleUrl}&seq=${dto.seq}">${dto.title}</a>
	                        </td>
	                        <td class="text-center">익명</td>
	                        <td class="text-center">${dto.regdate}</td>
	                        <td class="text-center">${dto.hit}</td> 
	                    </tr>
	                </c:forEach>
	                </tbody>
	            </table>
	        </div>
	
	        <div class="paging" style="text-align: center; min-height: 50px; line-height: 50px;">
	           <c:if test="${dataCount==0}">
	             등록된 게시물이 없습니다.
	           </c:if>
	           <c:if test="${dataCount!=0}">
	             ${paging}
	           </c:if>
	        </div>        
	        
	        <div style="clear: both;">
	        		<div style="float: left; width: 20%; min-width: 85px;">
	        		    <button type="button" class="btn btn-primary btn-sm bbtn"style="background-color: #8C8C8C" onclick="javascript:location.href='<%=cp%>/anonymity/list.do';">새로고침</button>
	        		</div>
	        		<div style="float: left; width: 60%; text-align: center;">
	        		     <form name="searchForm" method="post" class="form-inline">
							  <select class="form-control input-sm" name="searchKey" >
							      <option value="title">제목</option>
							     
							      <option value="content">내용</option>
							      <option value="regdate">날짜</option>
							  </select>
							  <input type="text" class="form-control input-sm input-search" name="searchValue">
							  <input type="hidden" name="rows" value="${rows}">
							  <button type="button" class="btn btn-primary btn-sm bbtn"style="background-color: #8C8C8C" onclick="searchList();"><span class="glyphicon glyphicon-search"></span> 검색</button>
	        		     </form>
	        		</div>
	        		<div style="float: left; width: 20%; min-width: 85px; text-align: right;">
	        		    <button type="button" class="btn btn-primary btn-sm bbtn" style="background-color: #8C8C8C" onclick="javascript:location.href='<%=cp%>/anonymity/created.do';">
	        		    <span class="glyphicon glyphicon glyphicon-pencil" ></span>글쓰기</button>
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