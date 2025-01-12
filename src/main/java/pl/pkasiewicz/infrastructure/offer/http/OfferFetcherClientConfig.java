package pl.pkasiewicz.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.pkasiewicz.domain.offer.OfferFetcher;

import java.time.Duration;

@Configuration
@AllArgsConstructor
public class OfferFetcherClientConfig {

    private final OfferFetcherRestTemplateConfigurationProperties properties;

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }

    @Bean
    public OfferFetcher remoteOfferFetcherClient(RestTemplate restTemplate) {
        return new OfferFetcherRestTemplate(restTemplate, properties.uri(), properties.port());
}
}
