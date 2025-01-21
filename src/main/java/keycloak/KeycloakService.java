package keycloak;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class KeycloakService {

    private static final Logger LOG = Logger.getLogger(KeycloakService.class);

    private final String keycloakUrl = "http://localhost:8081";
    private final String realm = "crab-fit-realm";
    private final String clientId = "client-api";
    private final String clientSecret = "UKbA0YGKgy0PBiIwQIaHbteHNNMk0ZRL";

    public String getAccessToken() {
        String tokenEndpoint = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        LOG.debug("Formed token endpoint URL: " + tokenEndpoint);

        WebTarget target = ClientBuilder.newClient().target(tokenEndpoint);

        Form form = new Form();
        form.param("client_id", clientId);
        form.param("client_secret", clientSecret);
        form.param("grant_type", "client_credentials");

        Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(Entity.form(form));

        LOG.debug("Token request status: " + response.getStatus());

        if (response.getStatus() == 200) {
            Map<String, Object> tokenResponse = response.readEntity(Map.class);
            LOG.debug("Token response: " + tokenResponse);
            return (String) tokenResponse.get("access_token");
        } else {
            String errorResponse = response.readEntity(String.class);
            LOG.error("Error response: " + errorResponse);
            throw new RuntimeException("Error al obtener el token: " + response.getStatus() + " - " + errorResponse);
        }
    }

    public String getUserIdByUsername(String username, String accessToken) {
        String userEndpoint = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username;
        LOG.debug("Formed user search endpoint URL: " + userEndpoint);

        WebTarget target = ClientBuilder.newClient().target(userEndpoint);

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .get();

        LOG.debug("User search request status: " + response.getStatus());

        if (response.getStatus() == 200) {
            List<Map<String, Object>> users = response.readEntity(List.class);
            if (users.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            String userId = (String) users.get(0).get("id");
            LOG.debug("User ID: " + userId);
            return userId;
        } else {
            String errorResponse = response.readEntity(String.class);
            LOG.error("Error response: " + errorResponse);
            throw new RuntimeException("Error al obtener el ID del usuario: " + response.getStatus() + " - " + errorResponse);
        }
    }

    public void assignRoleToUser(String userId, String roleId, String accessToken) {
        String roleMappingEndpoint = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
        LOG.debug("Formed role assignment endpoint URL: " + roleMappingEndpoint);

        WebTarget target = ClientBuilder.newClient().target(roleMappingEndpoint);

        Map<String, String> rolePayload = Map.of("id", roleId, "name", "student");

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(Entity.json(new Map[]{rolePayload}));

        LOG.debug("Role assignment request status: " + response.getStatus());

        if (response.getStatus() != 204) {
            String errorResponse = response.readEntity(String.class);
            LOG.error("Error response: " + errorResponse);
            throw new RuntimeException("Error al asignar el rol al usuario: " + response.getStatus() + " - " + errorResponse);
        }
    }

    public void assignRoleToUserCoach(String userId, String roleId, String accessToken) {
        String roleMappingEndpoint = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";
        LOG.debug("Formed role assignment endpoint URL: " + roleMappingEndpoint);

        WebTarget target = ClientBuilder.newClient().target(roleMappingEndpoint);

        Map<String, String> rolePayload = Map.of("id", roleId, "name", "coach");

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(Entity.json(new Map[]{rolePayload}));

        LOG.debug("Role assignment request status: " + response.getStatus());

        if (response.getStatus() != 204) {
            String errorResponse = response.readEntity(String.class);
            LOG.error("Error response: " + errorResponse);
            throw new RuntimeException("Error al asignar el rol al usuario: " + response.getStatus() + " - " + errorResponse);
        }
    }

    public void createUser(Map<String, Object> userDetails) {
        String accessToken = getAccessToken();
        String userEndpoint = keycloakUrl + "/admin/realms/" + realm + "/users";
        LOG.debug("Formed user endpoint URL: " + userEndpoint);

        WebTarget target = ClientBuilder.newClient().target(userEndpoint);

        Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .post(Entity.json(userDetails));

        LOG.debug("User creation request status: " + response.getStatus());

        if (response.getStatus() != 201) {
            String errorResponse = response.readEntity(String.class);
            LOG.error("Error response: " + errorResponse);
            throw new RuntimeException("Error al crear usuario: " + errorResponse);
        }
    }

}
