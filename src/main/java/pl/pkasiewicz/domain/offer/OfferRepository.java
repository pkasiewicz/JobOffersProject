package pl.pkasiewicz.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {

    Offer save(Offer offer);
    Optional<Offer> findById(String id);
    Optional<Offer> findByOfferUrl(String offerUrl);
    List<Offer> findAll();
    boolean existsByOfferUrl(String offerUrl);
    List<Offer> saveAll(List<Offer> offers);
}
