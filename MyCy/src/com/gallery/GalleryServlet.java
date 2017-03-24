package com.gallery;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/gallery/*")
public class GalleryServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	private SessionInfo info;
	private String pathname;
	private String cp;
	File f;

	GalleryDAO dao = new GalleryDAO();

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		cp = req.getContextPath();
		HttpSession session = req.getSession();
		info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// 이미지 파일 저장 경로		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + File.separator + "img";
		
		f = new File(pathname);
		if (!f.exists())
			f.mkdirs();

		if (uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {
			created(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			created_ok(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			update(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			update_ok(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}

	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		MyUtil util = new MyUtil();
		
		int maxHit = dao.maxHit();
		GalleryDTO hit_dto = dao.maxHitPhoto(maxHit);
		
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		int dataCount;
		int numPerPage = 6;
		int total_page;

		dataCount = dao.dataCount();

		total_page = util.pageCount(numPerPage, dataCount);
		if (current_page > total_page)
			current_page = total_page;

		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;

		List<GalleryDTO> list = dao.listBoard(start, end);

		String listUrl = cp + "/gallery/list.do";
		String articleUrl = cp + "/gallery/article.do?page=" + current_page;

		String paging = util.paging(current_page, total_page, listUrl);

		// list.jsp 페이지에 넘길 데이터
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);		
		req.setAttribute("hit_dto", hit_dto);	

		forward(req, resp, "/WEB-INF/views/gallery/list.jsp");
	}

	private void created(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/gallery/created.jsp");
	}

	private void created_ok(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String encType = "UTF-8";
		int maxSize = 5 * 1024 * 1024;
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());

		String saveFilename = mreq.getFilesystemName("upload");
		saveFilename = FileManager.doFilerename(pathname, saveFilename);

		GalleryDTO dto = new GalleryDTO();

		dto.setId(info.getId());
		dto.setTitle(mreq.getParameter("title"));
		dto.setContent(mreq.getParameter("content"));
		dto.setFilepath(saveFilename);

		dao.insertPhoto(dto);

		resp.sendRedirect(cp + "/gallery/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		MyUtil util = new MyUtil();

		int seq = Integer.parseInt(req.getParameter("seq"));
		String page = req.getParameter("page");

		dao.updateHitCount(seq);

		GalleryDTO dto = dao.readPhoto(seq);
		if (dto == null) {
			resp.sendRedirect(cp + "/gallery/list.do?page=" + page);
			return;
		}

		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		int dataCount;
		int numPerPage = 6;
		int total_page;

		dataCount = dao.dataCount();

		total_page = util.pageCount(numPerPage, dataCount);
		if (current_page > total_page)
			current_page = total_page;

		String articleUrl = cp + "/gallery/article.do?page=" + current_page;

		List<GalleryDTO> list = dao.loadPhotoList(dto.getSeq());

		req.setAttribute("list", list);
		req.setAttribute("articleUrl", articleUrl);
		
		forward(req, resp, "/WEB-INF/views/gallery/article.jsp");
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		int seq = Integer.parseInt(req.getParameter("seq"));
		String page = req.getParameter("page");

		GalleryDTO dto = dao.readPhoto(seq);
		if (dto == null || !dto.getId().equals(info.getId())) {
			resp.sendRedirect(cp + "/gallery/update.do?seq=" + seq + "&page=" + page);
			return;
		}

		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");
		req.setAttribute("page", page);

		forward(req, resp, "/WEB-INF/views/gallery/created.jsp");
	}

	private void update_ok(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		GalleryDTO dto = new GalleryDTO();

		String encType = "UTF-8";
		int maxSize = 5 * 1024 * 1024;
		MultipartRequest mreq = new MultipartRequest(req, pathname, maxSize, encType, new DefaultFileRenamePolicy());
		
		dto.setSeq(Integer.parseInt(mreq.getParameter("seq")));
		dto.setTitle(mreq.getParameter("title"));
		dto.setContent(mreq.getParameter("content"));
		dto.setFilepath(mreq.getParameter("filepath"));

		String page = mreq.getParameter("page");

		dao.updatePhoto(dto);

		resp.sendRedirect(cp + "/gallery/list.do?page=" + page);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		int seq = Integer.parseInt(req.getParameter("seq"));
		String page = req.getParameter("page");

		GalleryDTO dto = dao.readPhoto(seq);
		if (dto != null && dto.getId().equals(info.getId())) {
			FileManager.doFiledelete(pathname, dto.getFilepath());
			dao.deletePhoto(seq);
		}

		resp.sendRedirect(cp + "/gallery/list.do?page=" + page);
	}
}
