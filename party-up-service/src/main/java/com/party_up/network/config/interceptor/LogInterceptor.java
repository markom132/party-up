package com.party_up.network.config.interceptor;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.micrometer.common.lang.NonNull;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.party_up.network.config.log_cached_body.request.CachedBodyHttpServletRequest;
import com.party_up.network.config.log_cached_body.response.CachedBodyHttpServletResponse;
import com.party_up.network.model.RequestResponseLog;
import com.party_up.network.repository.RequestResponseLogRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Interceptor for logging request and response details.
 * <p>
 * This interceptor logs requests and responses for auditing and monitoring purposes,
 * excluding certain endpoints configured in {@link ExcludedEndpointsConfig}.
 * </p>
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    private final RequestResponseLogRepository requestResponseLogRepository;

    @Autowired
    private ExcludedEndpointsConfig excludedEndpointsConfig;

    /**
     * Constructs the LogInterceptor with the required repository.
     *
     * @param requestResponseLogRepository Repository for storing request-response logs.
     */
    public LogInterceptor(RequestResponseLogRepository requestResponseLogRepository) {
        this.requestResponseLogRepository = requestResponseLogRepository;
    }

    /**
     * Intercepts each incoming request to log its details before processing.
     *
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @param handler  the chosen handler to execute, for type and/or instance evaluation
     * @return true if the request should proceed to the handler, false otherwise
     * @throws IOException if an input or output error occurs while reading the request body
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        String requestUri = request.getRequestURI();
        log.info("Received request for URI: {}", requestUri);

        // Check if the endpoint is excluded from logging
        if (excludedEndpointsConfig.getExcludedEndpoint().contains(requestUri)) {
            log.info("Endpoint {} is excluded from logging.", requestUri);
            return true;
        }

        // Wrap request to cache the body for logging
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);

        String requestBody = new String(cachedBodyHttpServletRequest.getInputStream().readAllBytes());

        // Log request details
        RequestResponseLog logg = new RequestResponseLog();
        logg.setMethod(request.getMethod());
        logg.setEndpoint(request.getRequestURI());
        logg.setRequestBody(requestBody);
        logg.setTimestamp(LocalDateTime.now());

        // Save the log entry to the database
        requestResponseLogRepository.save(logg);
        log.info("Request details saved to log: [Method: {}, URI: {}]", request.getMethod(), requestUri);

        return true;
    }

    /**
     * Finalizes request logging after request completion, capturing response details.
     *
     * @param request  the current HTTP request
     * @param response the current HTTP response
     * @param handler  the chosen handler to execute
     * @param ex       any exception thrown on handler execution, if any; this does not
     *                 include exceptions that have been handled through an exception resolver
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) {
        String requestUri = request.getRequestURI();

        // Check if the endpoint is excluded from logging
        if (excludedEndpointsConfig.getExcludedEndpoint().contains(requestUri)) {
            return;
        }
        // Retrieve the wrapped response to access cached response body content
        CachedBodyHttpServletResponse responseWrapper =
                (CachedBodyHttpServletResponse) request.getAttribute("responseWrapper");

        if (responseWrapper != null) {
            String responseBody = new String(responseWrapper.getCachedContent());
            log.info("Captured response body for logging");

            // Find the latest log entry for this specific request URI and method
            RequestResponseLog logg = requestResponseLogRepository.
                    findTopByEndpointAndMethodOrderByTimestampDesc(request.getRequestURI(),
                            request.getMethod());

            if (logg != null) {
                LocalDateTime requestTimestamp = logg.getTimestamp();
                LocalDateTime responseTimestamp = LocalDateTime.now();
                Long executionTime = Duration.between(requestTimestamp, responseTimestamp).toMillis();

                // Populate log details with response data
                logg.setStatusCode(response.getStatus());
                logg.setResponseTimestamp(responseTimestamp);
                logg.setExecutionTime(executionTime);
                logg.setResponseBody(responseBody);

                try {
                    // Save the updated log entry
                    requestResponseLogRepository.save(logg);
                    log.info("Log saved successfully for URI: {}, with status: {}",
                            request.getRequestURI(), response.getStatus());
                } catch (Exception e) {
                    log.error("Error saving log for URI: {} - {}", request.getRequestURI(), e.getMessage());
                }
            } else {
                log.warn("No matching log entry found for URI: {}, Method: {}",
                        request.getRequestURI(), request.getMethod());
            }
        } else {
            log.warn("No response wrapper available, unable to capture response body.");
        }
    }

}
