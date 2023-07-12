package com.ehgus973.security1.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터");
        HttpServletRequest HttpServletRequest = (jakarta.servlet.http.HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (jakarta.servlet.http.HttpServletResponse) servletResponse;
        if(HttpServletRequest.getHeader("authorization2").equals("cos")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            PrintWriter out = servletResponse.getWriter();
            out.println("인증안됨");
        }
    }
}
