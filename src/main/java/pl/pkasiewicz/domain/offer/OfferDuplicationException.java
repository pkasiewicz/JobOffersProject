package pl.pkasiewicz.domain.offer;

import lombok.Getter;

import java.util.List;

@Getter
public class OfferDuplicationException extends RuntimeException {

    private final List<String> offerUrls;

    OfferDuplicationException(String offerUrl) {
        super(String.format("Offer with this url already exists: %s", offerUrl));
        this.offerUrls = List.of(offerUrl);
    }

    OfferDuplicationException(String message, List<Offer> offers) {
        super(String.format("error" + message + offers.toString()));
        this.offerUrls = offers.stream().map(Offer::offerUrl).toList();
    }
}
