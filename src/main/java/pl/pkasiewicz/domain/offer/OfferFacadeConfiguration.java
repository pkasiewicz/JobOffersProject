package pl.pkasiewicz.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OfferFacadeConfiguration {

    @Bean
    OfferFacade offerFacade(OfferRepository repository, OfferFetcher fetcher) {
        FetcherService service = new FetcherService(repository, fetcher);
        return new OfferFacade(repository, service);
    }
}
