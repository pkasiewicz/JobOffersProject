package pl.pkasiewicz.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.SampleJobOfferResponse;
import pl.pkasiewicz.domain.offer.dto.OfferResponseDto;
import pl.pkasiewicz.infrastructure.offer.scheduler.OfferFetcherScheduler;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserWantToSeeOffersIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("job-offers.offer-fetcher.http.client.config.port}", () -> wireMockServer.getPort());
        registry.add("job-offers.offer-fetcher.http.client.config.uri}", () -> WIRE_MOCK_HOST);
    }

    @Autowired
    public OfferFetcherScheduler offerFetcherScheduler;

    @Test
    public void user_want_to_see_offers_but_have_to_be_logged_in_and_external_server_should_have_some_offers() throws Exception {

        // #typical path: user want to see offers but have to be logged in and external server should have some offers
        // step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithNoOffersInJson())));


        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given && when
        List<OfferResponseDto> fetchedZeroNewOffers = offerFetcherScheduler.fetchOffers();
        // then
        assertThat(fetchedZeroNewOffers).isEmpty();


        // step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        // step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
        // step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC


        // step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // given
        String offersUrl = "/offers";
        // when
        ResultActions performGetResultWithNoOffers = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String jsonWithZeroOffers = performGetResultWithNoOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> zeroOffersInDatabase = objectMapper.readValue(jsonWithZeroOffers, new TypeReference<>() {
        });

        assertThat(zeroOffersInDatabase).isEmpty();


        // step 8: there are 2 new offers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get(offersUrl)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersInJson())));


        // step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // given && when
        List<OfferResponseDto> fetchedTwoNewOffers = offerFetcherScheduler.fetchOffers();
        OfferResponseDto expectedFirstOffer = fetchedTwoNewOffers.get(0);
        OfferResponseDto expectedSecondOffer = fetchedTwoNewOffers.get(1);
        // then
        assertThat(fetchedTwoNewOffers).hasSize(2);
        assertThat(fetchedTwoNewOffers).containsExactlyInAnyOrder(expectedFirstOffer, expectedSecondOffer);


        // step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // given && when
        ResultActions performGetResultWithTwoOffers = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        String jsonWithTwoOffers = performGetResultWithTwoOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> twoOffersInDatabase = objectMapper.readValue(jsonWithTwoOffers, new TypeReference<>() {
        });

        assertThat(twoOffersInDatabase).hasSize(2);
        assertThat(twoOffersInDatabase).containsExactlyInAnyOrder(expectedFirstOffer, expectedSecondOffer);


        // step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // given
        String notExistingId = "/9999";
        // when
        ResultActions performGetResultWithNotExistingId = mockMvc.perform(get(offersUrl + notExistingId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        performGetResultWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "message": "Offer with id [9999] not found",
                            "status": "NOT_FOUND"
                        }
                        """.trim()));


        // step 12: user made GET /offers/1000 and system returned OK(200) with offer
        // given
        String idOfExistingOffer = expectedFirstOffer.id();
        // when
        ResultActions performGetResultWithExistingId = mockMvc.perform(get(offersUrl + "/" + idOfExistingOffer)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        String jsonWithFoundOffer = performGetResultWithExistingId.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        OfferResponseDto savedOfferInDatabase = objectMapper.readValue(jsonWithFoundOffer, OfferResponseDto.class);

        assertThat(savedOfferInDatabase).isEqualTo(expectedFirstOffer);


        // step 13: there are 2 new offers in external HTTP server
        // given && when && then
        wireMockServer.stubFor(WireMock.get(offersUrl)
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersInJson())));


        // step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        // given && when
        List<OfferResponseDto> fetchedAnotherTwoNewOffers = offerFetcherScheduler.fetchOffers();
        OfferResponseDto expectedThirdOffer = fetchedAnotherTwoNewOffers.get(0);
        OfferResponseDto expectedFourthOffer = fetchedAnotherTwoNewOffers.get(1);
        // then
        assertThat(fetchedAnotherTwoNewOffers).hasSize(2);
        assertThat(fetchedAnotherTwoNewOffers).containsExactlyInAnyOrder(expectedThirdOffer, expectedFourthOffer);


        // step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // when
        ResultActions performGetResultWithFourOffers = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //then
        String jsonWithFourOffers = performGetResultWithFourOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> fourOffersInDatabase = objectMapper.readValue(jsonWithFourOffers, new TypeReference<>() {
        });

        assertThat(fourOffersInDatabase).hasSize(4);
        assertThat(fourOffersInDatabase).contains(expectedFirstOffer, expectedSecondOffer);


        // step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
        // given && when
        ResultActions performPostResultWithOffer = mockMvc.perform(post(offersUrl)
                .content("""
                        {
                            "companyName": "testCompany",
                            "position": "testPosition",
                            "salary": "9 000 - 10 000",
                            "offerUrl": "testUrl"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResultWithSavedOffer = performPostResultWithOffer.andExpect(status().isCreated()).andReturn();
        String jsonWithSavedOffer = mvcResultWithSavedOffer.getResponse().getContentAsString();
        OfferResponseDto savedOffer = objectMapper.readValue(jsonWithSavedOffer, OfferResponseDto.class);

        assertAll(
                () -> assertThat(savedOffer.id()).isNotNull(),
                () -> assertThat(savedOffer.companyName()).isEqualTo("testCompany"),
                () -> assertThat(savedOffer.position()).isEqualTo("testPosition"),
                () -> assertThat(savedOffer.salary()).isEqualTo("9 000 - 10 000"),
                () -> assertThat(savedOffer.offerUrl()).isEqualTo("testUrl")
        );


        // step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 5 offers
        // given && when
        ResultActions performGetResultWithOneOffers = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String jsonWithFiveOffers = performGetResultWithOneOffers.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<OfferResponseDto> fiveOffersInDatabase = objectMapper.readValue(jsonWithFiveOffers, new TypeReference<>() {
        });

        assertThat(fiveOffersInDatabase).hasSize(5);
    }

}
