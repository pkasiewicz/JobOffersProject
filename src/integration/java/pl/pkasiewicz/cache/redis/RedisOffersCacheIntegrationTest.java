package pl.pkasiewicz.cache.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import pl.pkasiewicz.BaseIntegrationTest;
import pl.pkasiewicz.SampleRequests;
import pl.pkasiewicz.domain.offer.OfferFacade;
import pl.pkasiewicz.infrastructure.loginandregister.controller.dto.JwtResponseDto;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RedisOffersCacheIntegrationTest extends BaseIntegrationTest {

    @Container
    private final static GenericContainer<?> REDIS = new GenericContainer<>("redis").withExposedPorts(6379);

    @SpyBean
    OfferFacade offerFacade;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    SampleRequests sampleRequests;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.redis.port", () -> REDIS.getFirstMappedPort().toString());
        registry.add("spring.cache.type", () -> "redis");
        registry.add("spring.cache.redis.time-to-live", () -> "PT1S");
    }

    @Test
    public void should_save_offers_to_cache_and_then_invalidate_by_time_to_live() throws Exception {
        // step 1: user was registered
        // given && when
        ResultActions registerUser = sampleRequests.performRegisterPostRequest();
        // then
        registerUser.andExpect(status().isCreated());


        // step 2: login
        // given && when
        ResultActions loginUser = sampleRequests.performSuccessLoginPostRequest();
        // then
        String json = loginUser.andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(json, JwtResponseDto.class);
        String token = jwtResponseDto.token();


        // step 3: should save offers to cache
        // given && when
        mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        verify(offerFacade, times(1)).findAllOffers();
        assertThat(cacheManager.getCacheNames().contains("jobOffers")).isTrue();


        // step 4: cache should be invalidated
        // given && when && then
        await().atMost(Duration.ofSeconds(4))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    mockMvc.perform(get("/offers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                    );
                    verify(offerFacade, times(2)).findAllOffers();
                });
    }
}
