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
		
		// 토큰: cos 만들어 줘야 함. id, pw 정상적으로 들어와서 로그인이 완료되면 토큰 만들어 주고 응답함
		// 요청할 마다 header에 Authorization에 value값으로 토큰 가지고 옴
		// 그때 토큰이 넘어오면 이 토큰이 내가 만든토큰인지 검증하면 됨 (RSA, HS256)
		if(req.getMethod().equals("POST")) {
		System.out.println("POST 요청됨");
		String headerAuth = req.getHeader("Authorization");
		System.out.println(headerAuth);
		System.out.println("필터1");
		
		if(headerAuth.equals("cos")) {
			chain.doFilter(req, res);
		}else {
			PrintWriter out = res.getWriter();
			out.println("sorry");
		}
		}
	}

}
