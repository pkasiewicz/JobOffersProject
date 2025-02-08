package pl.pkasiewicz.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.domain.loginandregister.dto.RegistrationResultDto;
import pl.pkasiewicz.infrastructure.loginandregister.controller.error.RegisterErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsernameDuplicateExceptionIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void should_return_409_conflict_when_trying_to_register_user_with_same_username() throws Exception {
        // step 1: register first user
        // given && when
        ResultActions firstRequest = mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String registeredUserJson = firstRequest.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RegistrationResultDto registrationResultDto = objectMapper.readValue(registeredUserJson, RegistrationResultDto.class);

        assertAll(
                () -> assertThat(registrationResultDto.username()).isEqualTo("someUser"),
                () -> assertThat(registrationResultDto.created()).isTrue(),
                () -> assertThat(registrationResultDto.id()).isNotNull()
        );


        // step 2:
        // given && when
        ResultActions secondRequest = mockMvc.perform(post("/register")
                .content("""
                        {
                            "username": "someUser",
                            "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        String duplicatedUserJson = secondRequest.andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString();
        RegisterErrorResponse registerErrorResponse = objectMapper.readValue(duplicatedUserJson, RegisterErrorResponse.class);

        assertThat(registerErrorResponse.message()).isEqualTo("User already exists");
    }
}
