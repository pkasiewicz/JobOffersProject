package pl.pkasiewicz.domain.offer;

import pl.pkasiewicz.domain.offer.dto.JobOfferDto;

import java.util.List;

class InMemoryOfferFetcher implements OfferFetcher {

    @Override
    public List<JobOfferDto> fetchOffers() {
        return List.of(
                new JobOfferDto("Java Developer", "Amazon", "8000-10000", "sampleUrl1"),
                new JobOfferDto("Senior Java Developer", "Google", "10000", "sampleUrl2"),
                new JobOfferDto("Junior Java Developer", "Apple", "7000", "sampleUrl3"),
                new JobOfferDto("Fullstack Developer", "Allegro", "12000", "sampleUrl4")
        );
    }
}
