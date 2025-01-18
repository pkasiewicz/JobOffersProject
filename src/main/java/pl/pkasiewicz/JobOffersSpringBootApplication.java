package pl.pkasiewicz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.pkasiewicz.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OfferFetcherRestTemplateConfigurationProperties.class})
@EnableMongoRepositories
public class JobOffersSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}
