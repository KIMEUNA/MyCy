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
		// 세션에 저장된 로그인 정보
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		AnonymityDAO dao=new AnonymityDAO();
		MyUtil util=new MyUtil();
		
		if(uri.indexOf("list.do")!=-1) {
			// 게시물 리스트
			String page=req.getParameter("page");
			int current_page=1;
			if(page!=null)
				current_page=Integer.parseInt(page);
			
			// 검색컬럼, 검색 값
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
			
			// 한화면에 출력할 게시물 수
			int numPerPage=10;
			String rows=req.getParameter("rows");
			if(rows!=null)
				numPerPage=Integer.parseInt(rows);

			// 데이터 개수
			int dataCount, total_page;
			if(searchValue.length()==0)
				dataCount=dao.dataCount();
			else
				dataCount=dao.dataCount(searchKey, searchValue);
			
			// 전체 페이지 수
			total_page=util.pageCount(numPerPage, dataCount);
			if(current_page>total_page)
				current_page=total_page;
			
			// 게시물 리스트
			int start=(current_page-1)*numPerPage+1;
			int end=current_page*numPerPage;
			List<AnonymityDTO> list;
			if(searchValue.length()==0)
				list=dao.listBoard(start, end);
			else
				list=dao.listBoard(start, end, searchKey, searchValue);
			
			// 게시물 리스트 번호 만들기
			int listNum, n=0;
			Iterator<AnonymityDTO> it=list.iterator();
			while(it.hasNext()) {
				AnonymityDTO dto=it.next();
				listNum=dataCount-(start+n-1);
				dto.setListNum(listNum);
				n++;
			}
			
			// 게시물리스트와 게시물보기 uri 및 파라미터
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
			
			// 페이징 처리
			String paging=util.paging(current_page, total_page, listUrl);
			
			// jsp에 넘길 데이터
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
			// 글쓰기 폼
			
			// 로그인이 안된 경우
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			req.setAttribute("mode", "created");
			forward(req, resp, "/WEB-INF/views/anonymity/created.jsp");
			
		} else if(uri.indexOf("created_ok.do")!=-1) {
			// 게시물 저장하기
			
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
			// 게시물 보기
			if(info==null) {
				forward(req, resp, "/WEB-INF/views/member/login.jsp");
				return;
			}
			
			// 게시물번호,페이지번호,rows [,searchKey,searchVlue]
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
			
			// 조회수 증가
			dao.updateHit(seq);
			
			// 게시물 가져오기
			AnonymityDTO dto=dao.readBoard(seq);
			if(dto==null) { // 게시물이 없으면 다시 리스트로
				resp.sendRedirect(cp+"/anonymity/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// 이전글 다음글
			AnonymityDTO preReadDto=dao.preReadBoard(dto.getGroupNum(), dto.getOrderNum(), 
					searchKey, searchValue);
			AnonymityDTO nextReadDto=dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNum(), 
					searchKey, searchValue);
			
			// 리스트나 이전글/다음글에서 사용할 파라미터
			String params="page="+page+"&rows="+rows;
			if(searchValue.length()!=0) {
				params+="&searchKey="+searchKey
						+"&searchValue="+URLEncoder.encode(searchValue, "utf-8");
			}
			
			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			req.setAttribute("params", params);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			
			forward(req, resp, "/WEB-INF/views/anonymity/article.jsp");
		} else if(uri.indexOf("reply.do")!=-1) {
			// 글 답변 폼
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
			
			String s="["+dto.getTitle()+"] 에 대한 답변입니다.";
			
			dto.setContent(s);
			
			req.setAttribute("dto", dto);
			req.setAttribute("mode", "reply");
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			forward(req, resp, "/WEB-INF/views/anonymity/created.jsp");
			
		} else if(uri.indexOf("reply_ok.do")!=-1) {
			// 답변 저장
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
			// 게시물 삭제
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
			// 글 수정 폼
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
			// 글 수정 완료
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

