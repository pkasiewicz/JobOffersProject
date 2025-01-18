package pl.pkasiewicz.domain.offer;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.StreamSupport;

class InMemoryOfferRepository implements OfferRepository {

    Map<String, Offer> database = new ConcurrentHashMap<>();

    @Override
    public <S extends Offer> S save(S entity) {
        if (database.values().stream().anyMatch(offer -> offer.offerUrl().equals(entity.offerUrl()))) {
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
        database.put(String.valueOf(id), offer);
        return (S) offer;
    }

    @Override
    public <S extends Offer> List<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .toList();
    }

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Offer> findAll() {
        return database.values()
                .stream()
                .toList();
    }

    @Override
    public Iterable<Offer> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Offer entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Offer> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Offer> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Offer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> S insert(S entity) {
        return null;
    }

    @Override
    public <S extends Offer> List<S> insert(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public <S extends Offer> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Offer> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Offer> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Offer> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Offer> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Offer, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public boolean existsByOfferUrl(String offerUrl) {
        return database.values()
                .stream()
                .anyMatch(offer -> offer.offerUrl().equals(offerUrl));
    }
}
