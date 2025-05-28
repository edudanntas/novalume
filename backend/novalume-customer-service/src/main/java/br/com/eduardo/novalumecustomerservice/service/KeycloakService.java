package br.com.eduardo.novalumecustomerservice.service;

import br.com.eduardo.novalumecustomerservice.infra.exception.custom.KeycloakCreateUserException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm-name}")
    private String REALM_NAME;

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
