package pl.pkasiewicz.infrastructure.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .securitySchemes(List.of(apiKeyScheme()))
                .securityContexts(List.of(securityContext()));
    }

    private ApiInfo apiInfo() {
        String description = "To authorize generate token using token-controller, click Authorize button and paste your token with \"Bearer \" at the beginning";
        return new ApiInfo("Job Offers", description, "1.0", null,
                new Contact("Piotr", "https://www.linkedin.com/in/piotr-kasiewicz/", "ptrkasiewicz@gmail.com"),
                "Github", "https://github.com/pkasiewicz", List.of());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(securityReferences())
                .build();
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = scope;
        return List.of(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKeyScheme() {
        return new ApiKey("Authorization", "Authorization", "header");
    }
}