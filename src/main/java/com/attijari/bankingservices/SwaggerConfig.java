package com.attijari.bankingservices;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi atividadeApi() {
        return GroupedOpenApi.builder()
                .group("Atividades API REST")
                .packagesToScan("com.attijari.bankingservices")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentation de l'API Bancaire")
                        .description("API pour la simulation du projet de développement de reconnaissance vocale pour l'accessibilité des personnes non-voyantes dans l'application eBanking d'Attijariwafa Bank")
                        .version("1.0")
                        .termsOfService("Conditions d'utilisation")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Attijariwafa")
                                .url("https://www.attijariwafabank.com/fr"))
                        .license(new License()
                                .name("Attijariwafa License Version 1.0")
                                .url("https://www.attijariwafabank.com/fr")))
                .externalDocs(new ExternalDocumentation()
                        .description("Autres liens")
                        .url("https://www.attijariwafabank.com/fr"));
    }
}
