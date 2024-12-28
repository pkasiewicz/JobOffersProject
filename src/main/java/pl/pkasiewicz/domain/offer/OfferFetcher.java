package pl.pkasiewicz.domain.offer;

import pl.pkasiewicz.domain.offer.dto.JobOfferDto;

import java.util.List;

interface OfferFetcher {
    List<JobOfferDto> fetchOffers();
}
