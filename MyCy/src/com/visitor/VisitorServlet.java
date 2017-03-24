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
		// session ��ü
		session = req.getSession();
		info=(SessionInfo)session.getAttribute("member");
		
		if(uri.indexOf("list.do")!=-1) {
			// ��Ϻ��̱�
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
	// �۾���
	protected void created_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// db�ְ�
		VisitorDTO dto=new VisitorDTO();
		dto.setId(info.getId());
		dto.setContent(req.getParameter("content"));
//		System.out.println(dto.getId());
		dao.insertVisitor(dto, "created");
		
		
		resp.sendRedirect(cp+"/visitor/list.do");		// �ߺ� x (�����ͺ����� ������)
		
	}
	// �ۺ���
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Ѿ�� ������
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null && page.length()!=0)
			current_page=Integer.parseInt(page);
		
		// ��ü�����Ͱ���
		int dataCount=dao.dataCount();
		
		// ��ü �������� ���ϱ�
		int numPerPage=5;
		int total_page=util.pageCount(numPerPage, dataCount);
		
		// ��ü���������� ǥ���� �������� ū���
		if(total_page<current_page)
			current_page=total_page;
				
		// �����õ������� ���۰� ��
		int start=(current_page-1)*numPerPage+1;
		int end=current_page*numPerPage;
				
		// ������ ��������
		List<VisitorDTO> list=dao.listVisitor(start, end);
		
		Iterator<VisitorDTO> it=list.iterator();
		while(it.hasNext()) {
			VisitorDTO dto=it.next();
			
			dto.setContent(dto.getContent().replaceAll(">", "&gt;"));
			dto.setContent(dto.getContent().replaceAll("<", "&lt;"));
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}
		// ����¡ó��
		String strUrl=cp+"/visitor/list.do";
		String paging=util.paging(current_page, total_page, strUrl);
				
		// guest.jsp�� �Ѱ��� ������
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
				
//		resp.sendRedirect(cp+"/visitor/list.do");		// �ߺ� x (�����ͺ����� ������)
		forward(req, resp, "/WEB-INF/views/visitor/list.jsp");	
		
		
	}	
	
	protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ����
		
		  if(info==null) {
              forward(req, resp, "/WEB-INF/views/member/login.jsp");
              return;
           }

		// ���� ��
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
			// �Խù��� �ø� ����ڰ� �ƴϸ�
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
		// ���� �Ϸ�
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
		// ����
		// ���� ����
		
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
