package com.amx.jax.payment;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.Constants;

@Component
public class WebTenantFilter implements Filter {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // empty
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = ((HttpServletRequest) req);
        String url = request.getRequestURI();
        String siteId = request.getParameter(TenantContextHolder.TENANT);

        LOGGER.info("Payment URL is :"+ url);
        
//        if (siteId != null && !Constants.BLANK.equals(siteId)) {
//            TenantContextHolder.setCurrent(siteId);
//        } else {
//            TenantContextHolder.setDefault();
//        }
        LOGGER.info("URL is :"+ url);
        LOGGER.info("site id  is :"+ siteId);
        
        TenantContextHolder.setCurrent(Tenant.BHR);
        
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // empty
    }
}
