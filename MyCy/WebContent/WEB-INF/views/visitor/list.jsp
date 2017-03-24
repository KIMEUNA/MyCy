<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("utf-8");

	String cp = request.getContextPath();
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

<title>방명록</title>


<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="<%=cp%>/res/bootstrap/css/bootstrap-theme.min.css"
	type="text/css" />

<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<link href="<%=cp%>/res/css/visitor/style.css" rel="stylesheet">

<link rel="stylesheet" href="<%=cp%>/res/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=cp%>/res/css/layout/layout.css"
	type="text/css">


<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>
<script src="<%=cp%>/res/js/ie10-viewport-bug-workaround.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="<%=cp%>/res/jquery/js/jquery.js"></script>
<script src="<%=cp%>/res/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">

	function sendVisitor() {
		<c:if test="${not empty member}">
		var f=document.visitorForm;

		f.action="<%=cp%>/visitor/created_ok.do";
		f.submit();		
		return true;
		</c:if>
	}
	
	function deleteVisitor() {
		<c:if test="${not empty member}">
		var f=document.frm;

		f.action="<%=cp%>/visitor/delete_ok.do";
		
		if(confirm("삭제 하시겠습니까 ?"))
			f.submit();
		return true;
		</c:if>
		
	}
	function updateVisitor() {
		<c:if test="${not empty member}">
		var f=document.frm;

		f.action="<%=cp%>/visitor/update.do";
		
		if(confirm("수정 하시겠습니까 ?"))
			f.submit();
		
		return true;
		</c:if>
	}
</script>
</head>

<body>
	<!-- ------------------------------------------  header ------------------------------------------- -->
	<div>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
	<!-- -----------------------------------------header end------------------------------------------ -->


	<div class="container" role="main">
		<div class="layoutMain">
			<!-- ------------------------------------------  title------------------------------------------- -->
			<div class="col-lg-12">
				<h3 class="page-header">
					<span class="glyphicon glyphicon-list-alt">방명록</span>
				</h3>
			</div>
			<!-- -----------------------------------------title end------------------------------------------ -->
			<div class="layoutBody">
				<div style="min-height: 450px;">
					<!-- 게스트 폼 시작 -->
					<div id="entry0Comment">
						<section id="guestbook" class="no-entry">
							<div class="content-width">
								<header class="hd cm-hd">
									<div class="hd-inner">
										<h2 class="hd-heading lts-narrow" role="heading">GuestBook</h2>
										<div class="sub-info">
											<!-- <span class="desc">방명록 써주세용♥</span> -->
											<div class="cm-content">
												<!-- guest-form { -->
												<div id="guest-form" class="write-form active"
													style="float: top;">
													<h3 class="sr-only">방명록 입력폼</h3>

													<form name="visitorForm" method="post"
														onsubmit="return sendVisitor();" style="margin: 0">
														
														<div class="textboxs focus-wrap focus">
															<label for="textarea-input" class="textarea-input"
																style="display: none;">글을 남겨주세요</label>
															<textarea name="content" required="required" rows="5"
																id="textarea-input" class="focus-target"
																placeholder="글을 남겨주세용"></textarea>
														</div>
														<!-- 		<div class="input-wrap clearfix">
			 												<div class="textbox focus-wrap">
																<label for="input-name">작성자<sup class="symbol">*</sup></label>
																<input type="text" id="input-name" class="focus-target"
																	name="id" value="" required>
															</div> 
														</div> -->


														<!-- 	공개/비공개						<div class="checkbox">
							    <input type="checkbox" name="" id="secret">
							    <label for="secret" class="clearfix">
							    	<span class="public">공개</span>
							    	<span class="secret">비공개</span>
							    </label>
							</div> -->
														<div class="submit-wrap">
															<!-- <button type="submit"
																onClick="addComment(this, 0); return false;"
																class="ccz-btn default">글남기기</button> -->
															<button type="submit" class="ccz-btn default">글남기기</button>
														</div>
													</form>
												</div>
											</div>
										</div>
									</div>
								</header>
							</div>
						</section>
					</div>

					<div id="listGuest" style="width: 600px; margin: 0px auto;">

<form name="frm" method="post" >
						<table
							style='width: 600px; margin: 10px auto 0px; border-spacing: 0px; border-collapse: collapse;'>
							<%-- 								<c:if test="${dataCount == 0}">
								jinjoun
								</c:if> --%>

							<c:forEach var="dto" items="${list}">
								<tr height='30' bgcolor='#EEEEEE'
									style='border: 1px solid #DBDBDB;'>
									<td width='50%' style='padding-left: 5px;'>작성자 | ${dto.id}</td>
									<td width='50%' align='right' style='padding-right: 5px;'>
										${dto.regdate} | <a onclick="updateVisitor()">수정</a> | <a onclick="deleteVisitor()">삭제</a> 
										<input type="hidden" name="seq" value="${dto.seq}">
										<input type="hidden" name="page" value="${page}">
									</td>
								</tr>

								<tr height='50'>
									<td colspan='2' style='padding: 5px;' valign='top'>${dto.content}</td>
								</tr>
							</c:forEach>
						</table>
						</form>


					</div>

				</div>

			</div>

		</div>

		<!-- 게스트 폼 끝 -->
		<div class="paging"
			style="text-align: center; min-height: 50px; line-height: 50px;">
			<c:if test="${dataCount==0 }">
                              등록된 게시물이 없습니다.
                         </c:if>
			<c:if test="${dataCount!=0 }">
                            ${paging}
                          </c:if>
		</div>
	</div>

	<!-- ----------------------------------------- footer------------------------------------------ -->
	<div>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
	<!-- -----------------------------------------footer end------------------------------------------ -->

	<script src="<%=cp%>/res/js/ie10-viewport-bug-workaround.js"></script>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

	<script src="<%=cp%>/res/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>