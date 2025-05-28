package br.com.eduardo.novalumecustomerservice.infra.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Bean
    Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9090")
                .realm("novalumestore")
                .clientId("internal-client")
                .clientSecret("TRszLQns4JcE4z6KSeTsY0pMAUDozJ0I")
                .grantType("client_credentials")
                .build();
    }
}
