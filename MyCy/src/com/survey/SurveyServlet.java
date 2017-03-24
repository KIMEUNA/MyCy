package com.survey;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/survey/*")
public class SurveyServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	HttpSession session;
	String uri;
	String cp;
	SessionInfo info;
	SurveyDAO dao = new SurveyDAO();
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		uri = req.getRequestURI();
		cp = req.getContextPath();

		// session 객체
		session = req.getSession();
		info = (SessionInfo) session.getAttribute("member");

		if (uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {
			created(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			created_ok(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("submit_ok")!= -1) {
			submit_ok(req, resp);
		} else if (uri.indexOf("stats.do")!=-1) {
			stats(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			update(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			update_ok(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);
		
		int numPerPage = 7;
		int dataCount = dao.dataCount();
		int total_page = util.pageCount(numPerPage, dataCount);
		
		if(current_page > total_page)
			current_page = total_page;
		
		int start = (current_page-1)*numPerPage+1;
		int end = current_page*numPerPage;
		
		List<SurveyDTO> list = dao.listBoard(start, end);
		
		String listUrl = cp + "/survey/list.do";
		String articleUrl = cp + "/survey/article.do?page=" + current_page;
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/survey/list.jsp");
	}

	private void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String item = req.getParameter("item");
		
		req.setAttribute("item", item);
		forward(req, resp, "/WEB-INF/views/survey/created.jsp");
	}

	private void created_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 글 등록하기.
		SurveyDTO dto = new SurveyDTO();
		dto.setId(info.getId());
		dto.setTitle(req.getParameter("title"));
		
		String[] questArr = req.getParameterValues("quest");
		for (int i = 0; i < questArr.length; i++) {
			dto.setQuest(questArr[i], i);
		}
		
		String choice = req.getParameter("choice");
		dto.setChoice(0);
		if(choice != null)
			dto.setChoice(1);
		
		String endDate = req.getParameter("enddate");
		endDate = endDate.replaceAll("T", " ");
		
		dto.setEnddate(endDate);
		dao.insertSurvey(dto);
		
		System.out.println(dto.toString());
		
		resp.sendRedirect(cp+"/survey/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int seq = Integer.parseInt(req.getParameter("seq"));
		String page = req.getParameter("page");
		
		dao.updateHit(seq);
		
		SurveyDTO dto = dao.getSurvey(seq);
		if (dto == null) { // 게시물이 없으면 다시 리스트로
			resp.sendRedirect(cp + "/survey/list.do?page=" + page);
			return;
		}
		
		List<String> quest = new ArrayList<>();
		for (int i = 0; i < dto.getQuestLength(); i++) {
			if(dto.getQuest(i) == null)
				break;
			quest.add(dto.getQuest(i));
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("quests", quest);
		req.setAttribute("page", page);
		
		if(dao.Chk(seq, info.getId())!=0) {
//			resp.setContentType("text/html;charset=utf-8");
//			PrintWriter out = resp.getWriter();
//			out.println("<script type='text/javascript'>");
//			out.println("alert('이미 투표에 참여하였습니다 !!!');");
////			out.println("history.back();");
//			out.println("</script>");
//			out.flush();
			
			resp.sendRedirect(cp + "/survey/stats.do?seq="+seq+"&page=" + page);
			
			return;
		}
		
		
		forward(req, resp, "/WEB-INF/views/survey/article.jsp");
		
	}

	private void submit_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 투표 완료 
		Survey_Sub dto = new Survey_Sub();
		dto.setId(info.getId());
		dto.setSeq(Integer.parseInt(req.getParameter("seq")));
		
		String[] answer = req.getParameterValues("answer");
		
		for (String ss : answer) 
			dto.setAnswer(1, Integer.parseInt(ss));
		
		dao.insertAnswer(dto);
		
		resp.sendRedirect(cp + "/survey/list.do");
	}
	
	private void stats(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int seq = Integer.parseInt(req.getParameter("seq"));
		String page = req.getParameter("page");
		
		dao.updateHit(seq);
		
		SurveyDTO dto = dao.getSurvey(seq);
		
		if (dto == null) { // 게시물이 없으면 다시 리스트로
			resp.sendRedirect(cp + "/survey/list.do?page=" + page);
			return;
		}
		
		List<String> quest = new ArrayList<>();
		for (int i = 0; i < dto.getQuestLength(); i++) {
			if(dto.getQuest(i) == null)
				break;
			quest.add(dto.getQuest(i));
		}
		
		int[] count = dao.countAnswer(seq);
		int total = dao.totalAnswer(seq);
		
		req.setAttribute("total", total);
		req.setAttribute("count", count);
		req.setAttribute("dto", dto);
		req.setAttribute("quests", quest);
		req.setAttribute("page", page);
		
		forward(req, resp, "/WEB-INF/views/survey/stats.jsp");
	}
	

	private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void update_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

}
