<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	request.setCharacterEncoding("utf-8");

	String cp = request.getContextPath();
%>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate value="${now}" pattern="yy-MM-dd hh:mm" var="today" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta name="description" content="">
<meta name="author" content="">
<link rel="icon" href="../../favicon.ico">

<title>우성오빵 게시판</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">

<script type="text/javascript">
function category() {
    var f=document.categoryForm;
    
    f.action="<%=cp%>
	/board/list.do";
		f.submit();
	}
</script>
 
</head>

<body>
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />
	</div>

	<div class="container" role="main">
		<div class="layoutMain">
			<div class="bodyFrame col-sm-10"
				style="float: none; margin-left: auto; margin-right: auto;">

				<div class="body-title">
					<h3>
						<span class="glyphicon glyphicon-book"></span> 투표 게시판
					</h3>
				</div>

				<div class="alert alert-info">
					<i class="glyphicon glyphicon-info-sign"></i> 우왕~~~~~~~
				</div>
				<div class="layoutBody">
					<div>
						<div style="clear: both; height: 35px; line-height: 35px;">
							<div style="float: left;">${dataCount}개(${total_page}페이지)</div>
						</div>

						<div class="table-responsive" style="clear: both;">
							<!-- 테이블 반응형 -->
							<table class="table table-hover">
								<thead>
									<tr>
										<th class="text-center" style="width: 100px;">
											<form name="categoryForm" action="<%=cp%>/board/list.do"
												method="post">
												<select class="form-control input-sm" name="category"
													onchange="category()">
													<option value="5" ${cate=='전체'?"selected='selected' ":""}>전체</option>
													<option value="5" ${cate=='일상'?"selected='selected' ":""}>일상</option>
													<option value="10" ${cate=='게임'?"selected='selected' ":""}>게임</option>
													<option value="20" ${cate=='장소'?"selected='selected' ":""}>장소</option>
													<option value="30" ${cate=='아침'?"selected='selected' ":""}>아침</option>
													<option value="30" ${cate=='점심'?"selected='selected' ":""}>점심</option>
													<option value="30" ${cate=='저녁'?"selected='selected' ":""}>저녁</option>
												</select>
											</form>
										</th>
										<th class="text-center">제목</th>
										<th class="text-center" style="width: 100px;">글쓴이</th>
										<th class="text-center" style="width: 100px;">날짜</th>
										<th class="text-center" style="width: 70px;">조회수</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="dto" items="${list}" varStatus="stat">
										<tr>
											<td class="text-center">${dto.seq}</td>
											<td class="text-center">
											<a href="${articleUrl}&seq=${dto.seq}">${dto.title}</a>
													<c:if test="${today < dto.enddate} ">
																	마감된 투표
																	</c:if>
																	</td>
											<td class="text-center">${dto.id}</td>
											<td class="text-center">${dto.regdate}</td>
											<td class="text-center">${dto.hit}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						<div class="paging"
							style="text-align: center; min-height: 50px; line-height: 50px;">
							<c:if test="${dataCount==0}">
                    등록된 게시물이 없습니다.
              </c:if>
							<c:if test="${dataCount!=0}">
                    ${paging}
              </c:if>
						</div>

						<div style="clear: both;">
							<div style="float: left; width: 90%; text-align: center;">
								<form name="searchForm" method="post" class="form-inline">
									<select class="form-control input-sm" name="searchKey">
										<option value="subject">제목</option>
										<option value="userName">작성자</option>
										<option value="content">내용</option>
										<option value="created">등록일</option>
									</select> <input type="text" class="form-control input-sm input-search"
										name="searchValue">
									<button type="button" class="btn btn-info btn-sm btn-search"
										onclick="searchList();">
										<span class="glyphicon glyphicon-search"></span> 검색
									</button>
								</form>
							</div>
							<div
								style="float: left; width: 10%; min-width: 85px; text-align: right;">
								<button type="button" class="btn btn-primary btn-sm bbtn"
									onclick="javascript:location.href='<%=cp%>/survey/created.do';">
									<span class="glyphicon glyphicon glyphicon-pencil"></span> 투표
									올리기
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</div>

</body>
</html>