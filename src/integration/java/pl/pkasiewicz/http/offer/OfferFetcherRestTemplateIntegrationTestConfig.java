package pl.pkasiewicz.http.offer;

import org.springframework.web.client.RestTemplate;
import pl.pkasiewicz.domain.offer.OfferFetcher;
import pl.pkasiewicz.infrastructure.offer.http.OfferFetcherRestTemplateConfig;
import pl.pkasiewicz.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;

public class OfferFetcherRestTemplateIntegrationTestConfig extends OfferFetcherRestTemplateConfig {

    public OfferFetcher remoteOfferFetcherClient(OfferFetcherRestTemplateConfigurationProperties properties) {
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler(), properties);
        return remoteOfferFetcherClient(restTemplate, properties);
    }
}
