package com.kt.smp.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class MothodFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
			, FilterChain filterChain) throws ServletException, IOException {
		
		String methodName = request.getMethod().toLowerCase();
		if (methodName.equals(HttpMethod.GET.name().toLowerCase())
				|| methodName.equals(HttpMethod.POST.name().toLowerCase())
				){
            filterChain.doFilter(request, response); 
        } else { 
        	response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        	response.getWriter().println("method not allowed");
        	response.getWriter().flush();
        }
	}
}
