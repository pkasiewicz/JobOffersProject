package pl.pkasiewicz.domain.offer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryOfferRepository implements OfferRepository {

    Map<String, Offer> database = new ConcurrentHashMap<>();

    @Override
    public Offer save(Offer entity) {
        if (this.existsByOfferUrl(entity.offerUrl())) {
            throw new OfferDuplicationException(entity.offerUrl());
        }
        int id = database.size() + 1;
        Offer offer = Offer.builder()
                .id(String.valueOf(id))
                .companyName(entity.companyName())
                .position(entity.position())
                .salary(entity.salary())
                .offerUrl(entity.offerUrl())
                .build();
        database.put(offer.id(), offer);
        return offer;
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<Offer> findByOfferUrl(String offerUrl) { //todo
//        return database.values().stream()
//                .filter(offer -> offerUrl.equals(offer.offerUrl()))
//                .findFirst();
        return Optional.of(database.get(offerUrl));
    }

    @Override
    public List<Offer> findAll() {
        return database.values()
                .stream()
                .toList();
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return database.values()
                .stream()
                .anyMatch(offer -> offer.offerUrl().equals(offerUrl));
    }

    @Override
    public List<Offer> saveAll(List<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .toList();
    }
}
