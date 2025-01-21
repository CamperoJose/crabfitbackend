package impl.utils.keycloak;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakConfig {

    public String getKeycloakUrl() {
        return "http://localhost:8081";
    }

    public String getRealm() {
        return "crab-fit-realm";
    }

    public String getClientId() {
        return "client-api";
    }

    public String getClientSecret() {
        return "UKbA0YGKgy0PBiIwQIaHbteHNNMk0ZRL";
    }
}