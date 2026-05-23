package com.ruoyi.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai-doctor.jwt")
@Data
public class WecomProps {
    private String secret;
    private long ttl;
    private String authHeader;
}
