package com.party_up.network.config.log_cached_body.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * A HttpServletResponse wrapper that caches the response body,
 * allowing it to be accessed multiple times.
 */
@Slf4j
public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();

    private ServletOutputStream outputStream;

    /**
     * Construct a CachedBodyHttpServletResponse to enable response caching.
     *
     * @param response the original HttpServletResponse to be wrapped
     */
    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
        log.debug("CachedBodyHttpServletResponse initialized for response caching.");
    }

    /**
     * Retrieves the ServletOutputStream and caches the written data,
     *
     * @return the ServletOutputStream for the response
     * @throws IOException if an I/O error occurs while obtaining the output stream
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (outputStream == null) {
            outputStream = new TeeServletOutputStream(super.getOutputStream(), cachedContent);
            log.debug("ServletOutputStream wrapped with caching output stream.");
        }
        return outputStream;
    }

    /**
     * Flushes the output stream buffer to ensure all data is written.
     *
     * @throws IOException if an I/O error occurs during flushing
     */
    @Override
    public void flushBuffer() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
            log.debug("Output stream buffer flushed.");
        }
    }

    /**
     * Returns the cached response content as a byte array.
     *
     * @return byte array of cached content
     */
    public byte[] getCachedContent() {
        log.debug("Cached content retrieved with size: {} bytes.", cachedContent.size());
        return cachedContent.toByteArray();
    }
}
