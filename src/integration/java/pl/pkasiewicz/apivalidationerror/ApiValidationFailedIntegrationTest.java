package pl.pkasiewicz.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.infrastructure.apivalidation.ApiValidationErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser
    public void should_return_400_bad_request_and_validation_message_when_request_for_offers_contains_null_and_empty_fields() throws Exception {
        // given && when
        ResultActions perform = mockMvc.perform(post("/offers")
                .content("""
                        {
                            "companyName": "",
                            "position": "",
                            "salary": ""
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String json = perform.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        assertThat(result.messages()).containsExactlyInAnyOrder(
                "companyName must not be empty",
                "position must not be empty",
                "salary must not be empty",
                "offerUrl must not be null",
                "offerUrl must not be empty");
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_for_register_user_contains_null_and_empty_fields() throws Exception {
        // given && when
        ResultActions perform = mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": ""
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String json = perform.andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);

        assertThat(result.messages()).containsExactlyInAnyOrder(
                "username must not be empty",
                "password must not be empty",
                "password must not be null"
        );
    }
}
