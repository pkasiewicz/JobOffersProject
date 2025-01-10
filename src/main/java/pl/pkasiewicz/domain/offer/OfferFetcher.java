package pl.pkasiewicz.domain.offer;

import pl.pkasiewicz.domain.offer.dto.JobOfferDto;

import java.util.List;

public interface OfferFetcher {
    List<JobOfferDto> fetchOffers();
}
