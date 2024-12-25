package com.party_up.network.config.log_cached_body;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.party_up.network.config.log_cached_body.request.RequestBodyCacheFilter;
import com.party_up.network.config.log_cached_body.response.ResponseCaptureFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class to register filters for caching request and response bodies.
 * This enables access to request and response data multiple times within the application.
 */
@Slf4j
@Configuration
public class FilterConfig {

    /**
     * Registers the RequestBodyCacheFilter to intercept all requests.
     * This filter caches the request body so it can be accessed multiple times.
     *
     * @return the FilterRegistrationBean for RequestBodyCacheFilter
     */
    @Bean
    public FilterRegistrationBean<RequestBodyCacheFilter> requestBodyCacheFilter() {
        FilterRegistrationBean<RequestBodyCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestBodyCacheFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        log.debug("RequestBodyCacheFilter registered for all URL patterns.");
        return registrationBean;
    }

    /**
     * Registers the ResponseCaptureFilter to intercept all responses.
     * This filter caches the response body for logging or other post-processing.
     *
     * @return the FilterRegistrationBean for ResponseCaptureFilter
     */
    @Bean
    public FilterRegistrationBean<ResponseCaptureFilter> responseBodyCacheFilter() {
        FilterRegistrationBean<ResponseCaptureFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseCaptureFilter());
        registrationBean.addUrlPatterns("/*"); // Apply to all URLs
        log.debug("ResponseCaptureFilter registered for all URL patterns.");
        return registrationBean;
    }
}
