package pl.pkasiewicz.infrastructure.offer.http;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.pkasiewicz.domain.offer.OfferFetcher;

import java.time.Duration;

@Configuration
public class OfferFetcherClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
                                     OfferFetcherRestTemplateConfigurationProperties properties) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }

    @Bean
    public OfferFetcher remoteOfferFetcherClient(RestTemplate restTemplate,
                                                 OfferFetcherRestTemplateConfigurationProperties properties) {
        return new OfferFetcherRestTemplate(restTemplate, properties.uri(), properties.port());
}
}
