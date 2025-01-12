package pl.pkasiewicz.infrastructure.offer.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.pkasiewicz.domain.offer.OfferFacade;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class OfferFetcherScheduler {

    private static final String STARTED_FETCHING_OFFERS = "Started fetching offers {}";
    private static final String ADDED_NEW_OFFERS = "Added new {} offers";
    private static final String FINISHED_FETCHING_OFFERS = "Finished fetching offers {}";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final OfferFacade offerFacade;

    @Scheduled(fixedDelayString = "${job-offers.offer-fetcher.scheduler.delay}")
    public List<OfferResponseDto> fetchOffers() {
        log.info(STARTED_FETCHING_OFFERS, LocalDateTime.now().format(formatter));
        final List<OfferResponseDto> addedOffers = offerFacade.fetchAndSaveAllOffersIfNotExists();
        log.info(ADDED_NEW_OFFERS, addedOffers.size());
        log.info(FINISHED_FETCHING_OFFERS, LocalDateTime.now().format(formatter));
        return addedOffers;
    }
}
