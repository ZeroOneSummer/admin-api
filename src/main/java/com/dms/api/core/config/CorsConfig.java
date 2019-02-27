package com.dms.api.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: S-sys-admin-api
 * @Date: 2018/12/13 0013 14:03
 * @Author: hushuangxi
 * @Description:
 */
@Component
public class CorsConfig implements Filter {
    Logger logger = LoggerFactory.getLogger(CorsConfig.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest)request;

        //logger.info("filter: request:{}|response:{}|method:{}|token:{}", req, resp, req.getMethod(), req.getHeader("token"));
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "origin, no-cache, x-requested-with, if-modified-since, pragma, last-modified, cache-control, expires, content-type, x-e4m-with,userid,token,access-control-allow-headers,Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("XDomainRequestAllowed","1");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
