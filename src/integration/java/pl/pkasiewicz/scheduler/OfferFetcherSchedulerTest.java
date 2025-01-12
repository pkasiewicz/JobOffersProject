package pl.pkasiewicz.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.JobOffersSpringBootApplication;
import pl.pkasiewicz.domain.offer.OfferFetcher;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobOffersSpringBootApplication.class, properties = "scheduling.enabled=true")
public class OfferFetcherSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferFetcher remoteOfferFetcher;

    @Test
    public void should_run_external_server_offers_fetching_exactly_given_times() {
        await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(
                        () -> verify(remoteOfferFetcher, times(2)).fetchOffers()
                );
    }

}
