package com.amx.jax.payment;

import static com.amx.jax.payment.PaymentConstant.BHR;
import static com.amx.jax.payment.PaymentConstant.KWT;
import static com.amx.jax.payment.PaymentConstant.OMN;

import java.io.IOException;
import java.util.StringTokenizer;

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

        LOGGER.info("URL is :"+ url);
        LOGGER.info("site id  is :"+ siteId);
        
        if (url != null) {
            StringTokenizer stok = new StringTokenizer(url, "/");

            while (stok.hasMoreTokens()) {
            	String tnt = stok.nextToken();
            	
                if (tnt.equalsIgnoreCase(BHR)) {
                    TenantContextHolder.setCurrent(Tenant.BHR);
                    LOGGER.info("Tenant is :"+ Tenant.BHR);
                    break;
                }else if (tnt.equalsIgnoreCase(OMN)) {
                    TenantContextHolder.setCurrent(Tenant.OMN);
                    LOGGER.info("Tenant is :"+ Tenant.OMN);
                    break;
                }else if (tnt.equalsIgnoreCase(KWT)) {
                    TenantContextHolder.setCurrent(Tenant.KWT);
                    LOGGER.info("Tenant is :"+ Tenant.KWT);
                    break;
                }
            }
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        // empty
    }
}
