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
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
<script type="text/javascript" src="<%=cp%>/res/js/util.js"></script>
<script type="text/javascript" src="<%=cp%>/res/jquery/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
function check() {
	var f = document.memberForm;
	var str;

	str=f.id.value;
	if(!/^[a-z][a-z0-9_]{4,9}$/i.test(str)) { 
		f.id.focus();
		return false;
	}
	
	str = f.pass.value;
	if(!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str)) { 
		f.pass.focus();
		return false;
	}
	
	if(f.passCheck.value != str) {
		$("#passCheck + .help-block").html("패스워드가 일치하지 않습니다.");
		f.passCheck.focus();
		return false;
	} else {
		$("#passCheck + .help-block").html("패스워드를 한번 더 입력해주세요.");
	}
	
    str = f.name.value;
	str = $.trim(str);
    if(!str) {
        f.name.focus();
        return false;
    }
    f.name.value = str;
	
    str = f.birth.value;
    if(!isValidDateFormat(str)) {
        f.birth.focus();
        return false;
    }

    str = f.email.value;
    if(!isValidEmail) {
        f.email.focus();
        return false;
    }
    
    str = f.tel1.value;
    if(!str) {
        f.tel1.focus();
        return false;
    }

    str = f.tel2.value;
    if(!/^(\d+)$/.test(str)) {
        f.tel2.focus();
        return false;
    }
    
    str = f.tel3.value;
    if(!/^(\d+)$/.test(str)) {
        f.tel3.focus();
        return false;
    }
    
    var mode="${mode}";
    if(mode=="created") {
    	f.action = "<%=cp%>/member/member_ok.do";
    } else if(mode=="update") {
    	f.action = "<%=cp%>/";
    }
    
    return true;
}
</script>

</head>
<body>

<div>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</div>

<div class="container" role="main">
  <div class="jumbotron">
    <h1><span class="glyphicon glyphicon-user"></span> ${mode=="created"?"회원 가입":"회원 정보 수정"}</h1>
    <p>SPRING의 회원이 되시면 회원님만의 유익한 정보를 만날수 있습니다.</p>
  </div>

  <div class="bodyFrame">
  <form class="form-horizontal" name="memberForm" method="post" onsubmit="return check();">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="id">아이디</label>
        <div class="col-sm-7">
            <input style="width: 30%" class="form-control" id="id" name="id" type="text" placeholder="아이디"
                       onchange="idCheck();"
                       value="${dto.id}"
                       ${mode=="update" ? "readonly='readonly' style='border:none;'":""}>
            <p class="help-block">아이디는 5~10자 이내이며, 첫글자는 영문자로 시작해야 합니다.</p>
        </div>
    </div>
 
    <div class="form-group">
        <label class="col-sm-2 control-label" for="pass">패스워드</label>
        <div class="col-sm-7">
            <input style="width: 30%" class="form-control" id="pass" name="pass" type="password" placeholder="비밀번호">
            <p class="help-block">패스워드는 5~10자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.</p>
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-2 control-label" for="passCheck">패스워드 확인</label>
        <div class="col-sm-7">
            <input style="width: 30%" class="form-control" id="passCheck" name="passCheck" type="password" placeholder="비밀번호 확인">
            <p class="help-block">패스워드를 한번 더 입력해주세요.</p>
        </div>
    </div>
 
    <div class="form-group">
        <label class="col-sm-2 control-label" for="name">이름</label>
        <div class="col-sm-7">
            <input style="width: 30%" class="form-control" id="name" name="name" type="text" placeholder="이름"
                       value="${dto.name}" ${mode=="update" ? "readonly='readonly' style='border:none;' ":""}>
        </div>
    </div>
 
    <div class="form-group">
        <label class="col-sm-2 control-label" for="birth">생년월일</label>
        <div class="col-sm-7" >
            <input style="width: 30%" class="form-control" id="birth" name="birth" type="date" placeholder="생년월일" value="${dto.birth}" >
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-2 control-label" for="gender">성별</label>
        <div class="col-sm-7">
        	<input id="gender" name="gender" type="radio" placeholder="성별" value="남자" checked="checked"> 남자 &nbsp;&nbsp;
        	<input id="gender" name="gender" type="radio" placeholder="성별" value="여자"> 여자
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-2 control-label" for="email">이메일</label>
        <div class="col-sm-7">
            <input style="width: 50%" class="form-control" id="email" name="email" type="email" placeholder="이메일" value="${dto.email}">
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-2 control-label" for="tel1">전화번호</label>
        <div class="col-sm-7">
             <div class="row">
                  <div class="col-sm-3" style="padding-right: 5px;">
						  <select class="form-control" id="tel1" name="tel1" >
								<option value="">선 택</option>
								<option value="010" ${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
								<option value="011" ${dto.tel1=="011" ? "selected='selected'" : ""}>011</option>
								<option value="016" ${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
								<option value="017" ${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
								<option value="018" ${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
								<option value="019" ${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
						  </select>
                  </div>

                  <div class="col-sm-1" style="width: 1%; padding-left: 5px; padding-right: 10px;">
                         <p class="form-control-static">-</p>
                  </div>
                 <div class="col-sm-2" style="padding-left: 5px; padding-right: 5px;">
 						  <input class="form-control" id="tel2" name="tel2" type="text" value="${dto.tel2}" maxlength="4">
                  </div>
                  <div class="col-sm-1" style="width: 1%; padding-left: 5px; padding-right: 10px;">
                         <p class="form-control-static">-</p>
                  </div>
                  <div class="col-sm-2" style="padding-left: 5px; padding-right: 5px;">
						  <input class="form-control" id="tel3" name="tel3" type="text" value="${dto.tel3}" maxlength="4">
                  </div>
             </div>
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-2 control-label" for="addr1">우편번호</label>
        <div class="col-sm-7">
             <div class="row">
                  <div class="col-sm-3" style="padding-right: 0px;">
						  <input class="form-control" id="addr1" name="addr1" type="text" value="${dto.addr1}" maxlength="7" readonly="readonly">
                  </div>
                  <div class="col-sm-1" style="width: 1%; padding-left: 5px; padding-right: 5px;">
                         <button type="button" class="btn btn-default">우편번호</button>
                  </div>
             </div>
        </div>
    </div>
    
    <div class="form-group">
        <label class="col-sm-2 control-label" for="addr1">주소</label>
        <div class="col-sm-7">
            <input style="width: 45%" class="form-control" id="addr2" name="addr2" type="text" placeholder="기본주소" readonly="readonly" value="${dto.addr2}">
            <input style="width: 70%" class="form-control" id="addr3" name="addr3" type="text" placeholder="나머지주소" value="${dto.addr3}" style="margin-top: 5px;">
        </div>
    </div>
    
<c:if test="${mode=='created'}">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="agree">약관 동의</label>
        <div class="col-sm-7 checkbox">
            <label>
                <input id="agree" name="agree" type="checkbox" checked="checked"
                         onchange="form.sendButton.disabled = !checked"> <a href="#">이용약관</a>에 동의합니다.
            </label>
        </div>
    </div>
</c:if>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
<c:if test="${mode=='created'}">
            <button type="submit" name="sendButton" class="btn btn-primary">회원가입 <span class="glyphicon glyphicon-ok"></span></button>
            <button type="button" class="btn btn-danger" onclick="javascript:location.href='<%=cp%>/';">가입취소 <span class="glyphicon glyphicon-remove"></span></button>
</c:if>            
<c:if test="${mode=='update'}">
            <button type="submit" class="btn btn-primary">정보수정 <span class="glyphicon glyphicon-ok"></span></button>
            <button type="button" class="btn btn-danger" onclick="javascript:location.href='<%=cp%>/';">수정취소 <span class="glyphicon glyphicon-remove"></span></button>
</c:if>            
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
                <p class="form-control-static">${message}</p>
        </div>
    </div>
     
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