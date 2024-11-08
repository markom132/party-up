package com.party_up.network.dto.mappers;

import com.party_up.network.model.RequestResponseLog;
import com.party_up.network.model.dto.RequestResponseLogDTO;
import com.party_up.network.model.dto.mappers.RequestResponseLogMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RequestResponseLogMapperTest {

    private RequestResponseLogMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new RequestResponseLogMapper();
    }

    @Test
    void testToDTO_withValidEntity() {
        RequestResponseLog log = new RequestResponseLog();
        log.setMethod("GET");
        log.setEndpoint("/api/test");
        log.setRequestBody("{\"request\":\"test\"}");
        log.setResponseBody("{\"response\":\"test\"}");
        log.setStatusCode(200);
        log.setTimestamp(LocalDateTime.now());
        log.setExecutionTime(100L);

        RequestResponseLogDTO dto = mapper.toDTO(log);

        assertNotNull(dto);
        assertEquals(log.getMethod(), dto.getMethod());
        assertEquals(log.getEndpoint(), dto.getEndpoint());
        assertEquals(log.getRequestBody(), dto.getRequestBody());
        assertEquals(log.getResponseBody(), dto.getResponseBody());
        assertEquals(log.getStatusCode(), dto.getStatusCode());
        assertEquals(log.getTimestamp(), dto.getTimestamp());
        assertEquals(log.getExecutionTime(), dto.getExecutionTime());
    }

    @Test
    void testToDTO_withNullEntity() {
        RequestResponseLogDTO dto = mapper.toDTO(null);
        assertNull(dto, "Expected null when converting a null RequestResponseLog entity to DTO.");
    }

    @Test
    void testToDtoList_withValidEntities() {
        RequestResponseLog log1 = new RequestResponseLog();
        log1.setMethod("POST");
        log1.setEndpoint("/api/login");

        RequestResponseLog log2 = new RequestResponseLog();
        log2.setMethod("GET");
        log2.setEndpoint("/api/data");

        List<RequestResponseLogDTO> dtoList = mapper.toDtoList(List.of(log1, log2));

        assertEquals(2, dtoList.size());
        assertEquals(log1.getMethod(), dtoList.get(0).getMethod());
        assertEquals(log2.getEndpoint(), dtoList.get(1).getEndpoint());
    }

    @Test
    void testToDtoList_withEmptyList() {
        List<RequestResponseLogDTO> dtoList = mapper.toDtoList(List.of());
        assertTrue(dtoList.isEmpty(), "Expected an empty list when converting an empty list of RequestResponseLog entities.");
    }

    @Test
    void testToEntity_withValidDTO() {
        RequestResponseLogDTO dto = new RequestResponseLogDTO(
                "PUT", "/api/update", "{\"key\":\"value\"}", "{\"result\":\"success\"}",
                200, LocalDateTime.now(), 150L
        );

        RequestResponseLog log = mapper.toEntity(dto);

        assertNotNull(log);
        assertEquals(dto.getMethod(), log.getMethod());
        assertEquals(dto.getEndpoint(), log.getEndpoint());
        assertEquals(dto.getRequestBody(), log.getRequestBody());
        assertEquals(dto.getResponseBody(), log.getResponseBody());
        assertEquals(dto.getStatusCode(), log.getStatusCode());
        assertEquals(dto.getTimestamp(), log.getTimestamp());
        assertEquals(dto.getExecutionTime(), log.getExecutionTime());
    }

    @Test
    void testToEntity_withNullDTO() {
        RequestResponseLog log = mapper.toEntity(null);
        assertNull(log, "Expected null when converting a null RequestResponseLogDTO to entity.");
    }
}
