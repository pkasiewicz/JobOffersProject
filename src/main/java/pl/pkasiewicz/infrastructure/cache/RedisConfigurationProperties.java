package pl.pkasiewicz.infrastructure.cache;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@ConfigurationProperties(prefix = "spring.redis")
public record RedisConfigurationProperties(
        String host,
        int port) {
}
