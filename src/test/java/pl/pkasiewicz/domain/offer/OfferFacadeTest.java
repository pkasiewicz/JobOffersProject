package pl.pkasiewicz.domain.offer;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import pl.pkasiewicz.domain.offer.dto.OfferRequestDto;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class OfferFacadeTest {

    private final OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeForTests();

    @Test
    void should_return_single_offer_by_id() {
        //given
        OfferRequestDto offerToSave = new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl");
        OfferResponseDto savedOffer = offerFacade.saveOffer(offerToSave);
        OfferResponseDto expected = new OfferResponseDto("1", "Amazon", "Java Developer", "8000-10000", "sampleUrl");
        //when
        OfferResponseDto actual = offerFacade.findOfferById(savedOffer.id());
        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    void should_throw_exception_when_offer_with_specified_id_does_not_exist() {
        //given
        String id = "100";
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferById(id));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining(String.format("Offer with id [%s] not found", id));
    }

    @Test
    void should_return_all_saved_offers() {
        //given
        List<OfferRequestDto> offersList = List.of(
                new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl"),
                new OfferRequestDto("Google", "Senior Java Developer", "10000", "sampleUrl2"),
                new OfferRequestDto("Apple", "Junior Java Developer", "7000", "sampleUrl3")
        );
        offersList.forEach(offerFacade::saveOffer);
        //when
        List<OfferResponseDto> actual = offerFacade.findAllOffers();
        //then
        assertThat(actual).hasSize(3);
    }

    @Test
    void should_return_empty_list_when_no_saved_offers() {
        //given

        //when
        List<OfferResponseDto> actual = offerFacade.findAllOffers();
        //then
        assertThat(actual).isEmpty();
    }

    @Test
    void should_return_saved_offer() {
        //given
        OfferRequestDto offerToSave = new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl");
        OfferResponseDto expected = new OfferResponseDto("1", "Amazon", "Java Developer", "8000-10000", "sampleUrl");
        //when
        OfferResponseDto actual = offerFacade.saveOffer(offerToSave);
        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    void should_throw_exception_when_offer_with_specified_url_already_exist() {
        //given
        OfferRequestDto expected = new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl");
        OfferRequestDto actual = new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl");
        offerFacade.saveOffer(expected);
        //when
        Throwable thrown = catchThrowable(() -> offerFacade.saveOffer(actual));
        //then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Offer with this url already exists: + [%s]", actual.offerUrl());
    }

    @Test
    void should_fetch_all_remote_offers_and_save_all_when_repository_is_empty() {
        //given
        assertThat(offerFacade.findAllOffers()).isEmpty();
        //when
        List<OfferResponseDto> result = offerFacade.fetchAndSaveAllOffersIfNotExists();
        //then
        assertThat(result).hasSize(4);
    }

    @Test
    void should_save_only_2_offers_when_repository_had_4_added_with_offers_url() {
        //given
        List<OfferRequestDto> existingOffers = List.of(
                new OfferRequestDto("Amazon", "Java Developer", "8000-10000", "sampleUrl1"),
                new OfferRequestDto("Google", "Senior Java Developer", "10000", "sampleUrl2")
        );
        existingOffers.forEach(offerFacade::saveOffer);
        //when
        List<OfferResponseDto> fetchedOffers = offerFacade.fetchAndSaveAllOffersIfNotExists();
        //then
        assertThat(List.of(
                        fetchedOffers.get(0).offerUrl(),
                        fetchedOffers.get(1).offerUrl()
                )
        ).containsExactlyInAnyOrder("sampleUrl3", "sampleUrl4");
    }
}