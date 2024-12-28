package pl.pkasiewicz.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferDto(String companyName,
                          String position,
                          String salary,
                          String offerUrl) {
}
