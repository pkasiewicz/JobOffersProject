package pl.pkasiewicz.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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

    @Autowired
    public OfferFetcherScheduler offerFetcherScheduler;

    @Test
    public void user_want_to_see_offers_but_have_to_be_logged_in_and_external_server_should_have_some_offers() throws Exception {
        //# typical path: user want to see offers but have to be logged in and external server should have some offers
        // step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        // given && when && then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithNoOffersInJson())));


        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given && when
        List<OfferResponseDto> fetchedZeroOffers = offerFetcherScheduler.fetchOffers();
        // then
        assertThat(fetchedZeroOffers).isEmpty();


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
        MvcResult mvcResultWithNoOffers = performGetResultWithNoOffers.andExpect(status().isOk()).andReturn();
        String jsonWithZeroOffers = mvcResultWithNoOffers.getResponse().getContentAsString();
        List<OfferResponseDto> zeroOffers = objectMapper.readValue(jsonWithZeroOffers, new TypeReference<>() {
        });
        assertThat(zeroOffers).isEmpty();


        // step 8: there are 2 new offers in external HTTP server
        // step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000


        // step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // given
        String notExistingId = "/9999";
        // when
        ResultActions performGetResultWithNotExistingId = mockMvc.perform(get(offersUrl + notExistingId));
        // then
        performGetResultWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            "message": "Offer with id [9999] not found",
                            "status": "NOT_FOUND"
                        }
                        """.trim()));


        // step 12: user made GET /offers/1000 and system returned OK(200) with offer
        //step 13: there are 2 new offers in external HTTP server
        //step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        //step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000


        //step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
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
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
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


        //step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        // given && when
        ResultActions performGetResultWithOneOffers = mockMvc.perform(get(offersUrl)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResultWithOneOffers = performGetResultWithOneOffers.andExpect(status().isOk()).andReturn();
        String jsonWithOneOffers = mvcResultWithOneOffers.  getResponse().getContentAsString();
        List<OfferResponseDto> jsonWithOneOffer = objectMapper.readValue(jsonWithOneOffers, new TypeReference<>() {
        });

        assertThat(jsonWithOneOffer).hasSize(1);
        assertThat(jsonWithOneOffer.stream().map(OfferResponseDto::id)).contains(savedOffer.id());

    }

}
