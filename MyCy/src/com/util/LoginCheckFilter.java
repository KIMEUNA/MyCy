package com.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;

@WebFilter("/*")
public class LoginCheckFilter implements Filter {
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
		throws IOException, ServletException {
		
		if(req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest)req;
			HttpSession session = request.getSession();
			SessionInfo info = (SessionInfo)session.getAttribute("member");
			
			String uri=request.getRequestURI();
			String cp=request.getContextPath();
			
			String[] uris={
					cp+"/index.jsp",
					cp+"/main.do",
					cp+"/res/",
					cp+"/mainImg/",
					cp+"/img/",
					cp+"/member/member.do",
					cp+"/member/member_ok.do",
					cp+"/member/login.do",
					cp+"/member/login_ok.do"
					};
			
			boolean b=false;
			for(String u : uris) {
				if(uri.startsWith(u)) {
					b=true;
					break;
				}
			}
			
			if(info!=null || uri.equals(cp+"/"))
				b=true;
			
			if(b) {
				chain.doFilter(req, resp);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/member/login.jsp");
				rd.forward(req, resp);
			}
			
		}

	}

	@Override
	public void destroy() {
	}

}
