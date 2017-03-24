package com.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.MyServlet;

@WebServlet("/member/*")
public class MemberServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	HttpSession session;
	String uri;
	String cp;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		uri = req.getRequestURI();
		cp = req.getContextPath();

		// session 객체 가져오기.
		session = req.getSession();

		if (uri.indexOf("login.do") != -1) {
			login(req, resp);
		} else if (uri.indexOf("login_ok.do") != -1) {
			login_ok(req, resp);
		} else if (uri.indexOf("logout.do") != -1) {
			logout(req, resp);
		} else if (uri.indexOf("member.do") != -1) {
			member(req, resp);
		} else if (uri.indexOf("member_ok.do") != -1) {
			member_ok(req, resp);
		}
	}

	private void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}

	private void login_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String pass = req.getParameter("pass");

		MemberDAO dao = new MemberDAO();

		MemberDTO dto = dao.getMember(id);
		if (dto == null || !dto.getPass().equals(pass)) {
			req.setAttribute("message", "아이디 또는 패스워드가 일치하지 않습니다.");
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}

		// 세션에 로그인 정보를 저장한다.
		SessionInfo info = new SessionInfo();

		info.setId(dto.getId());
		info.setName(dto.getName());

		session.setAttribute("member", info);

		resp.sendRedirect(cp);
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그아웃처리
		session.removeAttribute("member"); // 세션에 있는 정보를 지우기.
		session.invalidate(); // 세션 초기화.

		resp.sendRedirect(cp);
	}

	private void member(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 폼
		// req.setAttribute("title", "회원 가입");
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	private void member_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입 처리
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();

		dto.setId(req.getParameter("id"));
		dto.setPass(req.getParameter("pass"));
		dto.setName(req.getParameter("name"));
		dto.setBirth(req.getParameter("birth"));
		dto.setGender(req.getParameter("gender"));
		dto.setEmail(req.getParameter("email"));

		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");
		if (tel1 != null && tel1.length() != 0 && tel2 != null && tel2.length() != 0 && tel3 != null
				&& tel3.length() != 0) {
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);
		}

		// dto.setAddr1(req.getParameter("addr1"));
		// dto.setAddr2(req.getParameter("addr2"));
		dto.setAddr1("우편번호 해야되");
		dto.setAddr2("기본 주소 해야되");
		dto.setAddr3(req.getParameter("addr3"));

		int result = dao.insertMember(dto);

		if (result != 1) {
			String message = "회원 가입이 실패 했습니다.";

			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getName() + "</b>님 회원가입을 축하드립니다.<br>");
		sb.append("우성오빵과 함께 ~~<br>");

		req.setAttribute("title", "회원 가입");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}

}
