package pl.pkasiewicz.domain.offer.dto;

import lombok.Builder;

@Builder
public record JobOfferDto(String title,
                          String company,
                          String salary,
                          String offerUrl) {
}
