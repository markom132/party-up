package com.party_up.network.config.log_cached_body.request;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Filter that wraps the HttpServletRequest to cache the request body,
 * allowing it to be read multiple times in subsequent processing.
 */
@Slf4j
public class RequestBodyCacheFilter implements Filter {

    /**
     * Wraps the HttpServletRequest in a CachedBodyHttpServletRequest to enable repeated reading of the request body.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the filter chain to pass the request and response to the next filter
     * @throws IOException      if an I/O error occurs during request processing
     * @throws ServletException if an error occurs in the servlet processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.debug("RequestBodyCacheFilter invoked for URI: {}", httpRequest.getRequestURI());

        // Wrap the original request in CachedBodyHttpServletRequest
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
        log.debug("HttpServletRequest wrapped with CachedBodyHttpServletRequest.");

        // Pass the wrapped request through the filter chain
        chain.doFilter(wrappedRequest, response);
        log.debug("Request passed through filter chain.");
    }
}

