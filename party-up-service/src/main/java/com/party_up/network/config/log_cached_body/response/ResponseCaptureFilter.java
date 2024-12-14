package com.party_up.network.config.log_cached_body.response;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter that wraps the HttpServletResponse to allow capturing and caching the response body context.
 * This cached response is useful for logging or further processing after request completion.
 */
@Slf4j
public class ResponseCaptureFilter implements Filter {

    /**
     * Wraps the HttpServletResponse in a CachedBodyHttpServletResponse to capture response content.
     *
     * @param request  the incoming HttpServletRequest
     * @param response the outgoing HttpServletResponse, wrapped to capture body content
     * @param chain    the filter chain for processing the request
     * @throws IOException      if an I/O error occurs during processing
     * @throws ServletException if a servlet error occurs during processing
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        log.debug("Initializing response capture filter for request: {}",
                ((HttpServletRequest) request).getRequestURI());

        // Wraps the response to capture its content
        CachedBodyHttpServletResponse responseWrapper =
                new CachedBodyHttpServletResponse((HttpServletResponse) response);

        // Cast the request and set the wrapped response in the request attributes for later access
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpRequest.setAttribute("responseWrapper", responseWrapper);
        log.debug("CachedBodyHttpServletResponse set in request attributes.");

        chain.doFilter(httpRequest, responseWrapper);
        log.debug("Response capture completed for request: {}", httpRequest.getRequestURI());
    }
}


