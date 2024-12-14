package com.party_up.network.model.dto.mappers;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.party_up.network.model.RequestResponseLog;
import com.party_up.network.model.dto.RequestResponseLogDTO;

/**
 * Mapper for converting between RequestResponseLog entities and RequestResponseLogDTOs.
 */
@Slf4j
@Component
public class RequestResponseLogMapper {

    /**
     * Converts a RequestResponseLog entity to a RequestResponseLogDTO.
     *
     * @param requestResponseLog the RequestResponseLog entity to convert
     * @return RequestResponseLogDTO with log details, or null if requestResponseLog is null
     */
    public RequestResponseLogDTO toDTO(RequestResponseLog requestResponseLog) {
        if (requestResponseLog == null) {
            log.warn("Attempted to convert a null RequestResponseLog to RequestResponseLogDTO");
            return null;
        }

        return new RequestResponseLogDTO(
                requestResponseLog.getMethod(),
                requestResponseLog.getEndpoint(),
                requestResponseLog.getRequestBody(),
                requestResponseLog.getResponseBody(),
                requestResponseLog.getStatusCode(),
                requestResponseLog.getTimestamp(),
                requestResponseLog.getExecutionTime()
        );
    }

    /**
     * Converts a list of RequestResponseLog entities to a list of RequestResponseLogDTOs.
     *
     * @param logs the list of RequestResponseLog entities to convert
     * @return a list of RequestResponseLogDTOs
     */
    public List<RequestResponseLogDTO> toDtoList(List<RequestResponseLog> logs) {
        log.info("Converting a list of RequestResponseLog entities to list of " +
                "RequestResponseLogDTOs. Total logs: {}", logs.size());
        return logs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a RequestResponseLogDTO to a RequestResponseLog entity.
     *
     * @param requestResponseLogDTO the RequestResponseLogDTO to convert
     * @return RequestResponseLog entity, or null if RequestResponseLogDTO is null
     */
    public RequestResponseLog toEntity(RequestResponseLogDTO requestResponseLogDTO) {
        if (requestResponseLogDTO == null) {
            log.warn("Attempted to convert a null RequestResponseLogDTO to RequestResponseLog ");
            return null;
        }

        log.info("Converting RequestResponseLogDTO with method {} and endpoint {} to entity",
                requestResponseLogDTO.getMethod(), requestResponseLogDTO.getEndpoint());
        
        RequestResponseLog requestResponseLog = new RequestResponseLog();
        requestResponseLog.setMethod(requestResponseLogDTO.getMethod());
        requestResponseLog.setEndpoint(requestResponseLogDTO.getEndpoint());
        requestResponseLog.setRequestBody(requestResponseLogDTO.getRequestBody());
        requestResponseLog.setResponseBody(requestResponseLogDTO.getResponseBody());
        requestResponseLog.setStatusCode(requestResponseLogDTO.getStatusCode());
        requestResponseLog.setTimestamp(requestResponseLogDTO.getTimestamp());
        requestResponseLog.setExecutionTime(requestResponseLogDTO.getExecutionTime());

        return requestResponseLog;
    }
}
