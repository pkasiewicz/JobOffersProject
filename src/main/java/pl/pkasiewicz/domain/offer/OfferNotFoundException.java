package pl.pkasiewicz.domain.offer;

import lombok.Getter;

@Getter
public class OfferNotFoundException extends RuntimeException {

    private final String offerId;

    public OfferNotFoundException(String offerId) {
        super(String.format("Offer with this id does not exist: %s", offerId));
        this.offerId = offerId;
    }
}
