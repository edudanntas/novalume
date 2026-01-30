package br.com.eduardo.novalumecustomerservice.service;

import br.com.eduardo.novalumecustomerservice.dto.customer.CustomerLoginDto;
import br.com.eduardo.novalumecustomerservice.infra.exception.custom.KeycloakCreateUserException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;
    private final WebClient webClient = WebClient.builder().build();

    @Value("${keycloak.admin.realm-name}")
    private String REALM_NAME;
    @Value("${keycloak.token.client-secret}")
    private String CLIENT_SECRET;
    @Value("${keycloak.token.client-id}")
    private String CLIENT_ID;
    @Value("${keycloak.token.token-url}")
    private String TOKEN_URL;
    @Value("${keycloak.token.grant-type}")
    private String GRANT_TYPE;

    public String createKeycloakUser(String firstName, String lastName, String email, String password, String roleName) {
        String username = firstName.toLowerCase() + lastName.toLowerCase();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password);
        user.setCredentials(List.of(credentials));


        Response response = keycloak.realm(REALM_NAME).users().create(user);

        if (response.getStatus() != 201) {
            throw new KeycloakCreateUserException("Error to create user");
        }

        String location = response.getHeaders().get(HttpHeaders.LOCATION).toString();
        String userId = location.replace("[", "").replace("]", "");
        userId = userId.substring(userId.lastIndexOf("/") + 1);

        addRoleToUser(userId, roleName);

        return userId;
    }

    public String authenticate(CustomerLoginDto customerLoginDto) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", GRANT_TYPE);
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("username", customerLoginDto.email());
        params.add("password", customerLoginDto.password());

        String jsonResponse = webClient.post()
                .uri(TOKEN_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonResponse);
            return node.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error trying to process token: " + e.getMessage());
        }
    }

    private void addRoleToUser(String userId, String roleName) {
        RoleRepresentation role = keycloak.realm(REALM_NAME).roles()
                .get(roleName)
                .toRepresentation();

        if (role == null) {
            throw new NotFoundException(String.format("Role %s not found", roleName));
        }

        keycloak.realm(REALM_NAME).users()
                .get(userId)
                .roles()
                .realmLevel()
                .add(List.of(role));
    }
}
