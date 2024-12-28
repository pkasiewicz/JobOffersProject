package pl.pkasiewicz.domain.offer;

import pl.pkasiewicz.domain.offer.dto.JobOfferDto;
import pl.pkasiewicz.domain.offer.dto.OfferRequestDto;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;

class OfferMapper {

    static Offer mapToEntity(OfferRequestDto offerToSave) {
        return Offer.builder()
                .companyName(offerToSave.companyName())
                .position(offerToSave.position())
                .salary(offerToSave.salary())
                .offerUrl(offerToSave.offerUrl())
                .build();
    }

    static OfferResponseDto mapToOfferResponseDto(Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.id())
                .companyName(offer.companyName())
                .position(offer.position())
                .salary(offer.salary())
                .offerUrl(offer.offerUrl())
                .build();
    }

    static Offer mapToEntityFromJobOfferDto(JobOfferDto jobOfferDto) {
        return Offer.builder()
                .companyName(jobOfferDto.companyName())
                .position(jobOfferDto.position())
                .salary(jobOfferDto.salary())
                .offerUrl(jobOfferDto.offerUrl())
                .build();
    }
}
