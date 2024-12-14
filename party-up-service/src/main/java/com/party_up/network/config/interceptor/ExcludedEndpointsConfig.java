package com.party_up.network.config.interceptor;

import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Config class for managing endpoints that are excluded from logging.
 * <p>
 * The excluded endpoints are fetched from the application properties
 * under the property key `excluded.log.endpoints`.
 * </p>
 */
@Slf4j
@Component
public class ExcludedEndpointsConfig {

    @Value("${excluded.log.endpoints}")
    private String excludedEndpoint;

    /**
     * Retrieves a list of endpoints to be excluded from logging.
     * The list is derived by splitting the `excludedEndpoints` string on commas.
     *
     * @return List of excluded endpoint paths.
     */
    public List<String> getExcludedEndpoint() {
        List<String> excludedEndpoints = Arrays.asList(excludedEndpoint.split(","));
        log.debug("Excluded endpoints list: {}", excludedEndpoints);

        return excludedEndpoints;
    }
}
