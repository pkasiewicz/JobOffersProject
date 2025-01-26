package pl.pkasiewicz.infrastructure.offer.http;

import lombok.AllArgsConstructor;

import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.pkasiewicz.domain.offer.OfferFetcher;
import pl.pkasiewicz.domain.offer.dto.JobOfferDto;

@AllArgsConstructor
@Log4j2
public class OfferFetcherRestTemplate implements OfferFetcher {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public List<JobOfferDto> fetchOffers() {
        log.info("Started fetching offers using http client");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List<JobOfferDto>> response = getResponseEntity(requestEntity);
            final List<JobOfferDto> body = response.getBody();
            if (body == null) {
                log.error("Response body was null");
                throw new ResponseStatusException(HttpStatus.NO_CONTENT);
            }
            log.info("Success Response Body Returned: {}", body);
            return body;
        } catch (ResourceAccessException e) {
            log.error("Error while fetching offers using http client: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<List<JobOfferDto>> getResponseEntity(HttpEntity<HttpHeaders> requestEntity) {
        final String url = UriComponentsBuilder.fromHttpUrl(getUrlFromService()).toUriString();
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    private String getUrlFromService() {
        return uri + ":" + port + "/offers";
    }
}
