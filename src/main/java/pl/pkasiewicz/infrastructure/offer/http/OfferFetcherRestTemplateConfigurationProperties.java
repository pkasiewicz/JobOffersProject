package pl.pkasiewicz.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job-offers.offer-fetcher.http.client.config")
@Builder
public record OfferFetcherRestTemplateConfigurationProperties(long connectionTimeout,
                                                              long readTimeout,
                                                              String uri,
                                                              int port) {
}
