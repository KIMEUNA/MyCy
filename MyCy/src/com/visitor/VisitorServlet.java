package com.visitor;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/visitor/*")
public class VisitorServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	VisitorDAO dao=new VisitorDAO();
	VisitorDTO dto=new VisitorDTO();
	MyUtil util=new MyUtil();
	String cp;
	String uri;
	HttpSession session;
	SessionInfo info;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		uri = req.getRequestURI();
		cp = req.getContextPath();		
		// session 객체
		session = req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		
		if(uri.indexOf("list.do")!=-1) {
			// 목록보이기
			list(req, resp);
		}else if(uri.indexOf("created_ok.do")!=-1) {		
			created_ok(req, resp);
			
		} else if(uri.indexOf("update.do")!=-1) {
			update(req, resp);
		}else if(uri.indexOf("update_ok.do")!=-1) {
			update_ok(req, resp);
		} else if(uri.indexOf("delete_ok.do")!=-1) {
			delete_ok(req, resp);
		}
	}
	// 글쓰기
	protected void created_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// db넣고
		VisitorDTO dto=new VisitorDTO();
		dto.setId(info.getId());
		dto.setContent(req.getParameter("content"));
//		System.out.println(dto.getId());
		dao.insertVisitor(dto, "created");
		
		
		resp.sendRedirect(cp+"/visitor/list.do");		// 중복 x (데이터변동이 있을때)
		
	}
	// 글보기
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 넘어온 페이지
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null && page.length()!=0)
			current_page=Integer.parseInt(page);
		
		// 전체데이터개수
		int dataCount=dao.dataCount();
		
		// 전체 페이지수 구하기
		int numPerPage=5;
		int total_page=util.pageCount(numPerPage, dataCount);
		
		// 전체페이지보다 표시할 페이지가 큰경우
		if(total_page<current_page)
			current_page=total_page;
				
		// 가져올데이터의 시작과 끝
		int start=(current_page-1)*numPerPage+1;
		int end=current_page*numPerPage;
				
		// 데이터 가져오기
		List<VisitorDTO> list=dao.listVisitor(start, end);
		
		Iterator<VisitorDTO> it=list.iterator();
		while(it.hasNext()) {
			VisitorDTO dto=it.next();
			
			dto.setContent(dto.getContent().replaceAll(">", "&gt;"));
			dto.setContent(dto.getContent().replaceAll("<", "&lt;"));
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}
		// 페이징처리
		String strUrl=cp+"/visitor/list.do";
		String paging=util.paging(current_page, total_page, strUrl);
				
		// guest.jsp에 넘겨줄 데이터
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
				
//		resp.sendRedirect(cp+"/visitor/list.do");		// 중복 x (데이터변동이 있을때)
		forward(req, resp, "/WEB-INF/views/visitor/list.jsp");	
		
		
	}	
	
	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정
		
		  if(info==null) {
              forward(req, resp, "/WEB-INF/views/member/login.jsp");
              return;
           }

		// 수정 폼
			HttpSession session=req.getSession();
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			String cp=req.getContextPath();
			dao=new VisitorDAO();
			
			int seq=Integer.parseInt(req.getParameter("seq"));
			String pageNum=req.getParameter("page");
						
			if(dto==null) {
				resp.sendRedirect(cp+"/visitor/list.do?page="+pageNum);
				return;
			}
			
			VisitorDTO dto=dao.readVisitor(seq);
			// 게시물을 올린 사용자가 아니면
			if(! dto.getId().equals(info.getId())) {
				resp.sendRedirect(cp+"/visitor/list.do?page="+pageNum);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", pageNum);
			req.setAttribute("mode", "update");
			System.out.println("aaaaaa");
			String path="/WEB-INF/views/visitor/created.jsp";
			forward(req, resp, path);
  	  
//			resp.sendRedirect(cp+"/visitor/list.do?page="+pageNum);
	}
	
	protected void update_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		String cp = req.getContextPath();
		VisitorDAO dao=new VisitorDAO();

		String pageNum=req.getParameter("page");

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/visitor/list.do?page=" + pageNum);
			return;
		}

		VisitorDTO dto=new VisitorDTO();
		dto.setSeq(Integer.parseInt(req.getParameter("seq")));
		dto.setContent(req.getParameter("content"));

		dao.updateVisitor(dto);

		resp.sendRedirect(cp + "/visitor/list.do?page=" + pageNum);	
	}
	
	
	protected void delete_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		// 방명록 삭제
		
		  if(info==null) {
              forward(req, resp, "/WEB-INF/views/member/login.jsp");
              return;
           }
		
		int seq=Integer.parseInt(req.getParameter("seq"));
		String pageNum=req.getParameter("page");

		VisitorDAO dao=new VisitorDAO();

		dao.deleteVisitor(seq);
		
		resp.sendRedirect(cp+"/visitor/list.do?page="+pageNum);	
			
	}
}
