package pl.pkasiewicz.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@AllArgsConstructor
public class OfferFacadeConfiguration {

    private final OfferFetcher fetcher;

    @Bean
    OfferRepository offerRepository() {
        return new OfferRepository() {
            @Override
            public Offer save(Offer offer) {
                return null;
            }

            @Override
            public Optional<Offer> findById(String id) {
                return Optional.empty();
            }

            @Override
            public Optional<Offer> findByOfferUrl(String offerUrl) {
                return Optional.empty();
            }

            @Override
            public List<Offer> findAll() {
                return List.of();
            }

            @Override
            public boolean existsByOfferUrl(String offerUrl) {
                return false;
            }

            @Override
            public List<Offer> saveAll(List<Offer> offers) {
                return List.of();
            }
        };
    }

    @Bean
    FetcherService fetcherService() {
        return new FetcherService(offerRepository(), fetcher);
    }

    @Bean
    OfferFacade offerFacade(OfferRepository repository, FetcherService fetcherService) {
        return new OfferFacade(repository, fetcherService);
    }
}
