package pl.pkasiewicz.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.infrastructure.offer.controller.error.JobOfferPostErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class JobOfferDuplicateExceptionIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    @WithMockUser
    public void should_return_409_conflict_when_trying_to_add_offer_with_same_url() throws Exception {
        // step 1: saving first offer
        // given && when
        ResultActions firstRequest = mockMvc.perform(post("/offers")
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
        firstRequest.andExpect(status().isCreated());

        // step 2: trying to save second offer with same url
        // given && when
        ResultActions secondRequest = mockMvc.perform(post("/offers")
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
        String json = secondRequest.andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JobOfferPostErrorResponse result = objectMapper.readValue(json, JobOfferPostErrorResponse.class);

        assertThat(result.message()).isEqualTo("Offer with this url already exists");
    }
}
