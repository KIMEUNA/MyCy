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

		// session ��ü ��������.
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
			req.setAttribute("message", "���̵� �Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�.");
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}

		// ���ǿ� �α��� ������ �����Ѵ�.
		SessionInfo info = new SessionInfo();

		info.setId(dto.getId());
		info.setName(dto.getName());

		session.setAttribute("member", info);

		resp.sendRedirect(cp);
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α׾ƿ�ó��
		session.removeAttribute("member"); // ���ǿ� �ִ� ������ �����.
		session.invalidate(); // ���� �ʱ�ȭ.

		resp.sendRedirect(cp);
	}

	private void member(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ ��
		// req.setAttribute("title", "ȸ�� ����");
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	private void member_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ ó��
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
		dto.setAddr1("�����ȣ �ؾߵ�");
		dto.setAddr2("�⺻ �ּ� �ؾߵ�");
		dto.setAddr3(req.getParameter("addr3"));

		int result = dao.insertMember(dto);

		if (result != 1) {
			String message = "ȸ�� ������ ���� �߽��ϴ�.";

			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("<b>" + dto.getName() + "</b>�� ȸ�������� ���ϵ帳�ϴ�.<br>");
		sb.append("�켺������ �Բ� ~~<br>");

		req.setAttribute("title", "ȸ�� ����");
		req.setAttribute("message", sb.toString());

		forward(req, resp, "/WEB-INF/views/member/complete.jsp");
	}

}
