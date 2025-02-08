package pl.pkasiewicz.domain.offer;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import pl.pkasiewicz.domain.offer.dto.OfferRequestDto;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;
import pl.pkasiewicz.domain.offer.exceptions.OfferNotFoundException;

import java.util.List;

@AllArgsConstructor
public class OfferFacade {

    private final OfferRepository offerRepository;
    private final FetcherService fetcherService;

    public OfferResponseDto saveOffer(OfferRequestDto offerToSave) {
        Offer savedOffer = offerRepository.save(OfferMapper.mapToEntity(offerToSave));
        return OfferMapper.mapToOfferResponseDto(savedOffer);
    }

    public OfferResponseDto findOfferById(String id) {
        return offerRepository.findById(id)
                .map(OfferMapper::mapToOfferResponseDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }

    @Cacheable(cacheNames = "jobOffers")
    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapToOfferResponseDto)
                .toList();
    }

    public List<OfferResponseDto> fetchAndSaveAllOffersIfNotExists() {
        return fetcherService.fetchAndSaveAllOffersIfNotExists()
                .stream()
                .map(OfferMapper::mapToOfferResponseDto)
                .toList();
    }
}
