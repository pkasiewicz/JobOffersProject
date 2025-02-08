package pl.pkasiewicz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import pl.pkasiewicz.infrastructure.cache.RedisConfigurationProperties;
import pl.pkasiewicz.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;
import pl.pkasiewicz.infrastructure.security.jwt.JwtConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OfferFetcherRestTemplateConfigurationProperties.class,
        JwtConfigurationProperties.class, RedisConfigurationProperties.class})
@EnableMongoRepositories
public class JobOffersSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOffersSpringBootApplication.class, args);
    }
}
