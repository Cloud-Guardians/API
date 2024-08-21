package com.cloudians.global.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
	    
	    HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;

	    if (req.getMethod().equals("POST")) {
	        System.out.println("POST 요청됨");
	        String headerAuth = req.getHeader("Authorization");
	        System.out.println("Authorization Header: " + headerAuth);
	        
	        // 헤더가 없거나 "cos"인 경우
	        if (headerAuth == null || headerAuth.equals("cos")) {
	            // Authorization 헤더가 없거나 cos인 경우에는 필터 체인 계속 진행
	            chain.doFilter(req, res);
	        } else {
	            // Authorization 헤더가 있지만, 올바르지 않은 경우
	            PrintWriter out = res.getWriter();
	            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
	            out.println("Unauthorized: Invalid Authorization header.");
	        }
	    } else {
	        // POST가 아닐 경우 다음 필터로 진행
	        chain.doFilter(req, res);
	    }
	}
}
