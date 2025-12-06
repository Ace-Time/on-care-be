package org.ateam.oncare.logback.dto;

import java.time.LocalDateTime;

public class LogDTO {
    private LocalDateTime timestamp;
    private String clientIp;
    private String method;
    private String uri;
    private String referer;
    private String userAgent;

    public LogDTO(LocalDateTime timestamp, String clientIp, String method, String uri, String referer, String userAgent) {
        this.timestamp = timestamp;
        this.clientIp = clientIp;
        this.method = method;
        this.uri = uri;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getClientIp() { return clientIp; }
    public String getMethod() { return method; }
    public String getUri() { return uri; }
    public String getReferer() { return referer; }
    public String getUserAgent() { return userAgent; }

    @Override
    public String toString() {
        return String.format("LogEntry[timestamp=%s, clientIp=%s, method=%s, uri=%s, referer=%s, userAgent=%s]",
                timestamp, clientIp, method, uri, referer, userAgent);
    }
}
