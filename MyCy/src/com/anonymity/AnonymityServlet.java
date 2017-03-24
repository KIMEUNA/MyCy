package com.anonymity;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

@WebServlet("/anonymity/*")
public class AnonymityServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		
		String uri=req.getRequestURI();
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		// ���ǿ� ����� �α��� ����
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		AnonymityDAO dao=new AnonymityDAO();
		MyUtil util=new MyUtil();
		
		if(uri.indexOf("list.do")!=-1) {
			// �Խù� ����Ʈ
			String page=req.getParameter("page");
			int current_page=1;
			if(page!=null)
				current_page=Integer.parseInt(page);
			
			// �˻��÷�, �˻� ��
			String searchKey=req.getParameter("searchKey");
			String searchValue=req.getParameter("searchValue");
			if(searchKey==null) {
				searchKey="title";
				searchValue="";
			}
			if(req.getMethod().equalsIgnoreCase("GET")) {
				searchValue=URLDecoder.decode(
						searchValue, "UTF-8");
			}
			
			// ��ȭ�鿡 ����� �Խù� ��
			int numPerPage=10;
			String rows=req.getParameter("rows");
			if(rows!=null)
				numPerPage=Integer.parseInt(rows);

			// ������ ����
			int dataCount, total_page;
			if(searchValue.length()==0)
				dataCount=dao.dataCount();
			else
				dataCount=dao.dataCount(searchKey, searchValue);
			
			// ��ü ������ ��
			total_page=util.pageCount(numPerPage, dataCount);
			if(current_page>total_page)
				current_page=total_page;
			
			// �Խù� ����Ʈ
			int start=(current_page-1)*numPerPage+1;
			int end=current_page*numPerPage;
			List<AnonymityDTO> list;
			if(searchValue.length()==0)
				list=dao.listBoard(start, end);
			else
				list=dao.listBoard(start, end, searchKey, searchValue);
			
			// �Խù� ����Ʈ ��ȣ �����
			int listNum, n=0;
			Iterator<AnonymityDTO> it=list.iterator();
			while(it.hasNext()) {
				AnonymityDTO dto=it.next();
				listNum=dataCount-(start+n-1);
				dto.setListNum(listNum);
				n++;
			}
			
			// �Խù�����Ʈ�� �Խù����� uri �� �Ķ����
			String listUrl=cp+"/anonymity/list.do?rows="+numPerPage;
			String articleUrl=cp+"/anonymity/article.do?page="+
			           current_page+"&rows="+numPerPage;
			if(searchValue.length()!=0) {
				listUrl+="&searchKey="+searchKey
						+"&searchValue="
						+URLEncoder.encode(searchValue, "UTF-8");
				articleUrl+="&searchKey="+searchKey
						+"&searchValue="
						+URLEncoder.encode(searchValue, "UTF-8");
			}
			
			// ����¡ ó��
			String paging=util.paging(current_page, total_page, listUrl);
			
			// jsp�� �ѱ� ������
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("rows", numPerPage);
			req.setAttribute("searchKey", searchKey);
			req.setAttribute("searchValue", searchValue);
			
			forward(req, resp, "/WEB-INF/views/anonymity/list.jsp");
		} else if(uri.indexOf("created.do")!=-1) {
			// �۾��� ��
			
			// �α����� �ȵ� ���
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			req.setAttribute("mode", "created");
			forward(req, resp, "/WEB-INF/views/anonymity/created.jsp");
			
		} else if(uri.indexOf("created_ok.do")!=-1) {
			// �Խù� �����ϱ�
			
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			AnonymityDTO dto=new AnonymityDTO();
			
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setPass(req.getParameter("pass"));
			dao.insertBoard(dto, "created");
			
			resp.sendRedirect(cp+"/anonymity/list.do");
			
		} else if(uri.indexOf("article.do")!=-1) {
			// �Խù� ����
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			// �Խù���ȣ,��������ȣ,rows [,searchKey,searchVlue]
			int seq=Integer.parseInt(req.getParameter("seq"));
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			String searchKey=req.getParameter("searchKey");
			String searchValue=req.getParameter("searchValue");
			if(searchKey==null) {
				searchKey="title";
				searchValue="";
			}
			
			searchValue=URLDecoder.decode(searchValue, "utf-8");
			
			// ��ȸ�� ����
			dao.updateHit(seq);
			
			// �Խù� ��������
			AnonymityDTO dto=dao.readBoard(seq);
			if(dto==null) { // �Խù��� ������ �ٽ� ����Ʈ��
				resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// ������ ������
			AnonymityDTO preReadDto=dao.preReadBoard(dto.getGroupNum(), dto.getOrderNum(), 
					searchKey, searchValue);
			AnonymityDTO nextReadDto=dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNum(), 
					searchKey, searchValue);
			
			// ����Ʈ�� ������/�����ۿ��� ����� �Ķ����
			String params="page="+page+"&rows="+rows;
			if(searchValue.length()!=0) {
				params+="&searchKey="+searchKey
						+"&searchValue="+URLEncoder.encode(searchValue, "utf-8");
			}
			
			// JSP�� ������ �Ӽ�
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			req.setAttribute("params", params);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			
			forward(req, resp, "/WEB-INF/views/anonymity/article.jsp");
		} else if(uri.indexOf("reply.do")!=-1) {
			// �� �亯 ��
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			int seq=Integer.parseInt(req.getParameter("seq"));
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			AnonymityDTO dto=dao.readBoard(seq);
			if(dto==null) {
				resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			String s="["+dto.getTitle()+"] �� ���� �亯�Դϴ�.";
			
			dto.setContent(s);
			
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "reply");
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			forward(req, resp, "/WEB-INF/views/anonymity/created.jsp");
			
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			// �亯 ����
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			AnonymityDTO dto=new AnonymityDTO();
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setOrderNum(Integer.parseInt(req.getParameter("orderNum")));
			
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			dao.insertBoard(dto, "reply");
			
			resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
			
		} else if(uri.indexOf("delete.do")!=-1) {
			// �Խù� ����
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			int seq=Integer.parseInt(req.getParameter("seq"));
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			AnonymityDTO dto=dao.readBoard(seq);
//			if(dto!=null && (info.getId().equals("admin") || dto.getId().equals(info.getId())) ) {
//				dao.deleteBoard(seq);
//			}
			
			resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
			
		} else if(uri.indexOf("update.do")!=-1) {
			// �� ���� ��
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			int seq=Integer.parseInt(req.getParameter("seq"));
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			AnonymityDTO dto=dao.readBoard(seq);
//			if(dto==null || ! dto.getId().equals(info.getId())) {
//				resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
//				return;
//			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "update");
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			forward(req, resp, "/WEB-INF/views/anonymity/created.jsp");
			
		} else if(uri.indexOf("update_ok.do")!=-1) {
			// �� ���� �Ϸ�
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			AnonymityDTO dto=new AnonymityDTO();
			dto.setSeq(Integer.parseInt(req.getParameter("seq")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
		
			String page=req.getParameter("page");
			String rows=req.getParameter("rows");
			
			dao.updateBoard(dto);
			
			resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
		}
		
		
	}

}

