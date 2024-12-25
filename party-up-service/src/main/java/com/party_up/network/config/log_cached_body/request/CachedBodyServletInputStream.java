package com.party_up.network.config.log_cached_body.request;

import java.io.ByteArrayInputStream;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom ServletInputStream that wraps a cached body, allowing multiple reads of the request body.
 */
@Slf4j
public class CachedBodyServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream inputStream;

    /**
     * Initializes a CachedBodyServletInputStream with the cached request body.
     *
     * @param cachedBody byte array containing the cached request body
     */
    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.inputStream = new ByteArrayInputStream(cachedBody);
        log.debug("CachedBodyServletInputStream initialized with cached request body.");
    }

    /**
     * Checks if the input stream has been fully read.
     *
     * @return true if the input stream has no more data, false otherwise
     */
    @Override
    public boolean isFinished() {
        boolean finished = inputStream.available() == 0;
        log.debug("isFinished called. Result: {}", finished);
        return finished;
    }

    /**
     * Indicates readiness to read from the stream.
     *
     * @return true, as this stream is always ready to be read
     */
    @Override
    public boolean isReady() {
        log.debug("isReady called. Result: true");
        return true;
    }

    /**
     * Unsupported operation, as asynchronous reading is not implemented.
     *
     * @param readListener The non-blocking IO read listener
     * @throws UnsupportedOperationException as this operation is not supported
     */
    @Override
    public void setReadListener(ReadListener readListener) {
        log.warn("setReadListener called, but operation is not supported.");
        throw new UnsupportedOperationException("Non-blocking read is not supported.");
    }

    /**
     * Reads the next byte of data from the input stream.
     *
     * @return the next byte of data, or -1 if the end of stream is reached
     */
    @Override
    public int read() {
        int byteData = inputStream.read();
        log.debug("read called, Byte read: {}", byteData);
        return byteData;
    }
}
