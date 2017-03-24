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

<title>다이어리</title>

<link href="<%=cp%>/res/bootstrap/css/bootstrap.min.css"
   rel="stylesheet">
<link href="<%=cp%>/res/css/layout/justified-nav.css" rel="stylesheet">
<script src="<%=cp%>/res/js/ie-emulation-modes-warning.js"></script>

<script type="text/javascript" src="<%=cp%>/res/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
   function searchList() {
      var f=document.searchForm;
      f.action="<%=cp%>/diary/list.do";
      f.submit();
   }
   
   function article(seq) {
      var url="${articleUrl}&seq="+seq;
      location.href=url;
   }
</script>

</head>

<body>
   <div>
      <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
   </div>

<div class="container" role="main">

    <!-- Header Carousel -->
    <header id="myCarousel" class="carousel slide">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
            <li data-target="#myCarousel" data-slide-to="1"></li>
            <li data-target="#myCarousel" data-slide-to="2"></li>
        </ol>

        <!-- Wrapper for slides -->
        <div class="carousel-inner">
            <div class="item active">
                <div class="fill" style="background-image:url('http://placehold.it/1900x1080&text=Slide One');"></div>
                <div class="carousel-caption">
                    <h2>Caption 1</h2>
                </div>
            </div>
            <div class="item">
                <div class="fill" style="background-image:url('http://placehold.it/1900x1080&text=Slide Two');"></div>
                <div class="carousel-caption">
                    <h2>Caption 2</h2>
                </div>
            </div>
            <div class="item">
                <div class="fill" style="background-image:url('http://placehold.it/1900x1080&text=Slide Three');"></div>
                <div class="carousel-caption">
                    <h2>Caption 3</h2>
                </div>
            </div>
        </div>

        <!-- Controls -->
        <a class="left carousel-control" href="#myCarousel" data-slide="prev">
            <span class="icon-prev"></span>
        </a>
        <a class="right carousel-control" href="#myCarousel" data-slide="next">
            <span class="icon-next"></span>
        </a>
    </header>

    <!-- Page Content -->
    <div class="container">

        <!-- Marketing Icons Section -->
        <div class="row">
            <div class="col-lg-12">
                <h2 class="page-header">
                   <span class="glyphicon glyphicon-pencil"></span> 다이어리
                </h2>
            </div>
            
            <c:forEach var="dto" items="${list}" >
               <div class="col-md-4">
                  <div class="panel panel-default">
                     <div class="panel-heading">
                        <h4>
                           <i class="fa fa-fw fa-check"></i><a
                              href='${articleUrl}&seq=${dto.seq}'></a>No&nbsp;${dto.seq}.&nbsp;${dto.title}</h4>
                     </div>

                     <div class="panel-body">
                        <table width="100%" cellspacing="0" cellpadding="0">
                           <tr>
                              <td align="center" colspan="2">
                                 <p style=" height: 130px; overflow: hidden;">${dto.content}</p>
                              </td>
                           </tr>
                           <tr>
                           <td align="left"><span class="glyphicon glyphicon-eye-open">&nbsp;${dto.hit}&nbsp;</span></td>
                              <td align="left"><a class="btn btn-default"
                                 onclick="javascript:article('${dto.seq}');">더보기</a></td>
                           </tr>
                        </table>

                     </div>
                  </div>
               </div>
            </c:forEach>
            
           </div>  
           
           <%-- <div style="clear: both;">
               <div align="center">
               <c:if test="${dataCount==0 }">
                     등록된 게시물이 없습니다.
               </c:if>
               <c:if test="${dataCount!=0 }">
                  <ul class="pagination pagination-sm">
                        <li class="disabled"><a href="#">&laquo;</a></li>
                        <li class="active"><a style="background-color: #010b26"
                           href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li><a href="#">&raquo;</a></li>
                     </ul>
               </c:if> --%>
               
            <div class="paging" style="text-align: center; min-height: 50px; line-height: 50px;">
               <c:if test="${dataCount==0 }">
                     등록된 게시물이 없습니다.
               </c:if>
               <c:if test="${dataCount!=0 }">
                   ${paging}
               </c:if>

                  </div>
                  <div style="float: left; width: 98%; min-width: 80px; text-align: right;">
                        <button type="button" class="btn btn-primary btn-sm bbtn" onclick="javascript:location.href='<%=cp%>/diary/regdate.do';"><span class="glyphicon glyphicon glyphicon-pencil"></span> 글쓰기</button>
                 </div>
                 <div style="float: left; width: 100%; text-align: center;">
                        <form name="searchForm" method="post" class="form-inline">
                          <select class="form-control input-sm" name="searchKey" >
                              <option value="title">제목</option>
                              <option value="id">작성자</option>
                              <option value="content">내용</option>
                              <option value="regdate">등록일</option>
                          </select>
                          <input type="text" class="form-control input-sm input-search" name="searchValue">
                      <button type="button" class="btn btn-info btn-sm btn-search" onclick="searchList();"><span class="glyphicon glyphicon-search"></span> 검색</button>
                      </form>
                 </div>
           </div>
        </div>
        <!-- /.row -->

        <hr>

        <!-- Call to Action Section -->
        <div class="well">
            <div class="row">
                <div class="col-md-8">
                    <p></p>
                </div>
                <div class="col-md-2" style="float: right">
                    <a class="btn btn-lg btn-default btn-block" href="#" >
                    <p><span class="glyphicon glyphicon-chevron-up"> 맨위로</span></p>
                    </a>
                </div>
            </div>
        </div>

        <hr>

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