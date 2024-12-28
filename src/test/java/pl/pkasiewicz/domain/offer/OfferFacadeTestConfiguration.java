package pl.pkasiewicz.domain.offer;

public class OfferFacadeTestConfiguration {

    private final InMemoryOfferRepository repository;
    private final InMemoryOfferFetcher fetcher;

    OfferFacadeTestConfiguration() {
        this.repository = new InMemoryOfferRepository();
        this.fetcher = new InMemoryOfferFetcher();
    }

    OfferFacade offerFacadeForTests() {
        return new OfferFacade(repository, new FetcherService(repository, fetcher));
    }
}
