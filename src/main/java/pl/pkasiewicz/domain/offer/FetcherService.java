package pl.pkasiewicz.domain.offer;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class FetcherService{

    private final OfferRepository offerRepository;
    private final OfferFetcher offerFetcher;

    public List<Offer> fetchAndSaveAllOffersIfNotExists() {
        List<Offer> fetchedOffers = fetchOffers();
        List<Offer> filteredOffers = filterOnlyNonExistingOffers(fetchedOffers);
        try {
            return offerRepository.saveAll(filteredOffers);
        } catch (OfferDuplicationException e) {
            throw new OfferDuplicationException(e.getMessage(), filteredOffers);
        }
    }

    private List<Offer> fetchOffers() {
        return offerFetcher.fetchOffers()
                .stream()
                .map(OfferMapper::mapToEntityFromJobOfferDto)
                .toList();
    }

    private List<Offer> filterOnlyNonExistingOffers(List<Offer> offers) {
        return offers.stream()
                .filter(offer -> !offer.offerUrl().isEmpty())
                .filter(offer -> !offerRepository.existsByOfferUrl(offer.offerUrl()))
                .toList();
    }
}
