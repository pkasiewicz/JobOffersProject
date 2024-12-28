package pl.pkasiewicz.domain.offer;

import pl.pkasiewicz.domain.offer.dto.JobOfferDto;

import java.util.List;

class InMemoryOfferFetcher implements OfferFetcher {

    @Override
    public List<JobOfferDto> fetchOffers() {
        return List.of(
                new JobOfferDto("Amazon", "Java Developer", "8000-10000", "sampleUrl1"),
                new JobOfferDto("Google", "Senior Java Developer", "10000", "sampleUrl2"),
                new JobOfferDto("Apple", "Junior Java Developer", "7000", "sampleUrl3"),
                new JobOfferDto("Allegro", "Fullstack Developer", "12000", "sampleUrl4")
        );
    }
}
