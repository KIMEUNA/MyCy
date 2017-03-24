package com.diary;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/diary/*")
public class DiaryServlet extends MyServlet{
	private static final long serialVersionUID = 1L;
	private SessionInfo info;
	private String cp;
	
	DiaryDAO dao = new DiaryDAO();
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		String uri = req.getRequestURI();
		cp = req.getContextPath();

		HttpSession session = req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		
		if(info==null) { 
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}

		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("regdate.do")!=-1) {
			regdateForm(req, resp);
		}  else if(uri.indexOf("regdate_ok.do")!=-1) {
			regdateSubmit(req, resp);
		}  else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("reply.do")!=-1) {
			replyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			replySubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		MyUtil util=new MyUtil();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null)
			current_page=Integer.parseInt(page);
		
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="title";
			searchValue="";
		}

		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue=URLDecoder.decode(searchValue, "utf-8");
		}
		
		int dataCount;
		if(searchValue.length()==0)
			dataCount=dao.dataCount();
		else
			dataCount=dao.dataCount(searchKey, searchValue);
		
		int numPerPage=6;
		int total_page=util.pageCount(numPerPage, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		int start=(current_page-1)*numPerPage+1;
		int end=current_page*numPerPage;
		
		List<DiaryDTO> list=null;
		if(searchValue.length()==0)
			list=dao.listDiary(start, end);
		else
			list=dao.listDiary(start, end, searchKey, searchValue);
		String params="";
		if(searchValue.length()!=0) {

			searchValue=URLEncoder.encode(searchValue, "utf-8");
			params="searchKey="+searchKey+
					 "&searchValue="+searchValue;
		}
		
		String listUrl=cp+"/diary/list.do";
		String articleUrl=cp+"/diary/article.do?page="+current_page;
		if(params.length()!=0) {
			listUrl+="?"+params;
			articleUrl+="&"+params;
		}
		
		String paging=util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("paging", paging);
		req.setAttribute("articleUrl", articleUrl);

		String path="/WEB-INF/views/diary/list.jsp";
		forward(req, resp, path);
	}
	
	private void regdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("mode", "regdate");
		String path="/WEB-INF/views/diary/created.jsp";
		forward(req, resp, path);
	}
	
	private void regdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		DiaryDTO dto=new DiaryDTO();
		
		dto.setId(info.getId());
		
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		
		dao.insertDiary(dto, "regdate");
		
		resp.sendRedirect(cp+"/diary/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int seq=Integer.parseInt(req.getParameter("seq"));
		String page=req.getParameter("page");
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="title";
			searchValue="";
		}
		
		searchValue=URLDecoder.decode(searchValue, "utf-8");
		
		dao.updateHit(seq);
		
		DiaryDTO dto=dao.readDiary(seq);
		if(dto==null) { 
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}
		
		int linesu=dto.getContent().split("\n").length;
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		
		DiaryDTO preReadDto=dao.preReadDiary(dto.getGroupNum(), dto.getOrderNum(), 
				searchKey, searchValue);
		DiaryDTO nextReadDto=dao.nextReadDiary(dto.getGroupNum(), dto.getOrderNum(), 
				searchKey, searchValue);
		
		String params="page="+page;
		if(searchValue.length()!=0) {
			params+="&searchKey="+searchKey
					+"&searchValue="+URLEncoder.encode(searchValue, "utf-8");
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("linesu", linesu);
		req.setAttribute("params", params);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		
		String path="/WEB-INF/views/diary/article.jsp";
		forward(req, resp, path);
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String page=req.getParameter("page");
		int seq=Integer.parseInt(	req.getParameter("seq"));
		DiaryDTO dto=dao.readDiary(seq);
		
		if(dto==null) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}
		
		if(! dto.getId().equals(info.getId())) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "update");
		
		String path="/WEB-INF/views/diary/created.jsp";
		forward(req, resp, path);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String page=req.getParameter("page");
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}
		
		DiaryDTO dto=new DiaryDTO();
		dto.setSeq(Integer.parseInt(req.getParameter("seq")));
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		
		dao.updateDiary(dto);
		
		resp.sendRedirect(cp+"/diary/list.do?page="+page);
	}

	private void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int seq=Integer.parseInt(req.getParameter("seq"));
		String page=req.getParameter("page");
		DiaryDTO dto=dao.readDiary(seq);
		if(dto==null) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}
		dto.setContent("["+dto.getTitle()+"] 에 대한 답변입니다.\n");
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		req.setAttribute("mode", "reply");
		
		String path="/WEB-INF/views/diary/created.jsp";
		forward(req, resp, path);
	}

	private void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String page=req.getParameter("page");
		
		DiaryDTO dto=new DiaryDTO();
		dto.setId(info.getId());
		
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));
		dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
		dto.setDepth(Integer.parseInt(req.getParameter("depth")));
		dto.setOrderNum(Integer.parseInt(req.getParameter("orderNum")));
		
		dao.insertDiary(dto, "reply");
		resp.sendRedirect(cp+"/diary/list.do?page="+page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String page=req.getParameter("page");
		int seq=Integer.parseInt(req.getParameter("seq"));
		DiaryDTO dto=dao.readDiary(seq);
		
		if(dto==null) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}

		if(! dto.getId().equals(info.getId()) && ! info.getId().equals("admin")) {
			resp.sendRedirect(cp+"/diary/list.do?page="+page);
			return;
		}

		dao.deleteDiary(seq);
		resp.sendRedirect(cp+"/diary/list.do?page="+page);
	}		
}
