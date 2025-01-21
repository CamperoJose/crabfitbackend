package impl.utils.keycloak;

import org.jboss.logging.Logger;
import dto.UserRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Objects;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class KeycloakService {

    private static final Logger LOG = Logger.getLogger(KeycloakService.class);

    @Inject
    KeycloakConfig config;

    public String getAccessToken() {
        String tokenEndpoint = config.getKeycloakUrl() + "/realms/" + config.getRealm() + "/protocol/openid-connect/token";
        Form form = new Form()
                .param("client_id", config.getClientId())
                .param("client_secret", config.getClientSecret())
                .param("grant_type", "client_credentials");

        Response response = ClientBuilder.newClient().target(tokenEndpoint)
                .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(Entity.form(form));

        if (response.getStatus() == 200) {
            return response.readEntity(Map.class).get("access_token").toString();
        } else {
            throw new KeycloakServiceException("Error al obtener el token: " + response.getStatus());
        }
    }


    public void createAndAssignRole(UserRequestDTO userRequest, String roleName) {
        Logger logger = Logger.getLogger(getClass());

        if (userRequest == null) {
            throw new IllegalArgumentException("El objeto UserRequestDTO no puede ser null");
        }
        Objects.requireNonNull(userRequest.firstName, "El campo 'firstName' no puede ser null");
        Objects.requireNonNull(userRequest.lastName, "El campo 'lastName' no puede ser null");
        Objects.requireNonNull(userRequest.email, "El campo 'email' no puede ser null");
        Objects.requireNonNull(userRequest.username, "El campo 'username' no puede ser null");
        Objects.requireNonNull(userRequest.password, "El campo 'password' no puede ser null");
        Objects.requireNonNull(userRequest.phoneNumber, "El campo 'phoneNumber' no puede ser null");
        Objects.requireNonNull(userRequest.address, "El campo 'address' no puede ser null");

        String accessToken = getAccessToken();
        logger.infof("Token de acceso obtenido: Bearer %s",
                accessToken.substring(0, Math.min(10, accessToken.length())) + "...");

        Map<String, Object> userPayload = Map.of(
                "firstName", userRequest.firstName,
                "lastName", userRequest.lastName,
                "email", userRequest.email,
                "username", userRequest.username,
                "enabled", true,
                "credentials", List.of(
                        Map.of(
                                "type", "password",
                                "value", userRequest.password,
                                "temporary", false
                        )
                ),
                "attributes", Map.of(
                        "phone_number", userRequest.phoneNumber,
                        "adress", userRequest.address
                )
        );

        String userEndpoint = config.getKeycloakUrl() + "/admin/realms/" + config.getRealm() + "/users";

        logger.infof("Endpoint para crear usuario: %s", userEndpoint);
        logger.infof("Payload para crear usuario: %s", userPayload);

        try {
            Response response = ClientBuilder.newClient().target(userEndpoint)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken)
                    .post(Entity.json(userPayload));

            logger.infof("Estado de la respuesta al crear usuario: %d", response.getStatus());
            String responseBody = response.readEntity(String.class);
            logger.infof("Respuesta al crear usuario: %s", responseBody);

            if (response.getStatus() != 201) {
                throw new KeycloakServiceException("Error al crear usuario: " + response.getStatus() + " - " + responseBody);
            }

            String userId = getUserIdByUsername(userRequest.username, accessToken);
            logger.infof("ID del usuario obtenido: %s", userId);

            assignRoleToUser(userId, roleName, accessToken);
            logger.infof("Rol '%s' asignado exitosamente al usuario con ID: %s", roleName, userId);

        } catch (Exception e) {
            logger.error("Error al crear usuario o asignar rol.", e);
            throw new KeycloakServiceException("Error al crear usuario y asignar rol: " + e.getMessage(), e);
        }
    }

    public String getUserIdByUsername(String username, String accessToken) {
        String userEndpoint = config.getKeycloakUrl() + "/admin/realms/" + config.getRealm() + "/users?username=" + username;
        Response response = ClientBuilder.newClient().target(userEndpoint)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Bearer " + accessToken)
                .get();

        if (response.getStatus() == 200) {
            List<Map<String, Object>> users = response.readEntity(List.class);
            if (!users.isEmpty()) {
                return users.get(0).get("id").toString();
            }
        }
        throw new KeycloakServiceException("Usuario no encontrado.");
    }


    public void assignRoleToUser(String userId, String roleName, String accessToken) {
        Logger logger = Logger.getLogger(getClass());

        Map<String, String> roles = Map.of(
                "student", "35c7411a-5f30-46f5-92f0-d6b79d65174f",
                "coach", "521c8197-2701-4643-8bec-9837dfc1745d"
        );

        String roleId = roles.getOrDefault(roleName, roles.get("student")); // Default a "student" si no se encuentra

        String roleEndpoint = config.getKeycloakUrl() + "/admin/realms/" + config.getRealm() + "/users/" + userId + "/role-mappings/realm";

        List<Map<String, String>> rolePayload = List.of(
                Map.of(
                        "id", roleId,
                        "name", roleName
                )
        );

        logger.infof("Asignando rol al usuario: %s", userId);
        logger.infof("Endpoint: %s", roleEndpoint);
        logger.infof("Payload: %s", rolePayload);
        logger.infof("Access Token: Bearer %s", accessToken.substring(0, Math.min(10, accessToken.length())) + "...");

        try {
            Response response = ClientBuilder.newClient().target(roleEndpoint)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Authorization", "Bearer " + accessToken)
                    .post(Entity.json(rolePayload));

            logger.infof("Estado de respuesta: %d", response.getStatus());
            String responseBody = response.readEntity(String.class);
            logger.infof("Respuesta de Keycloak: %s", responseBody);

            if (response.getStatus() != 204) {
                throw new KeycloakServiceException("Error al asignar rol: " + response.getStatus() + " - " + responseBody);
            }

            logger.info("Rol asignado exitosamente.");
        } catch (Exception e) {
            logger.error("Error al asignar rol al usuario.", e);
            throw new KeycloakServiceException("Error al asignar rol: " + e.getMessage(), e);
        }
    }


}
