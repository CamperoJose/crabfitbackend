package keycloak;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class);

    @Inject
    KeycloakService keycloakService;

    @POST
    @Path("/student")
    @PermitAll
    public Response createStudentUser(Map<String, Object> userDetails) {
        LOG.debug("Received request to create user: " + userDetails);

        try {
            Map<String, Object> keycloakUser = new HashMap<>();
            keycloakUser.put("firstName", userDetails.get("firstName"));
            keycloakUser.put("lastName", userDetails.get("lastName"));
            keycloakUser.put("email", userDetails.get("email"));
            keycloakUser.put("username", userDetails.get("username"));
            keycloakUser.put("enabled", true); // Valor predeterminado
            keycloakUser.put("credentials", new Object[] {
                    Map.of(
                            "type", "password",
                            "value", userDetails.get("password"),
                            "temporary", false
                    )
            });

            keycloakUser.put("attributes", Map.of(
                    "phone_number", userDetails.get("phone_number"),
                    "adress", userDetails.get("adress")
            ));

            String accessToken = keycloakService.getAccessToken();
            keycloakService.createUser(keycloakUser);
            String userId = keycloakService.getUserIdByUsername((String) userDetails.get("username"), accessToken);
            String studentRoleId = "35c7411a-5f30-46f5-92f0-d6b79d65174f"; // ID del rol 'student'
            keycloakService.assignRoleToUser(userId, studentRoleId, accessToken);

            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error al crear usuario: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/coach")
    @RolesAllowed("admin")
    public Response createCoachUser(Map<String, Object> userDetails) {
        LOG.debug("Received request to create user: " + userDetails);

        try {
            Map<String, Object> keycloakUser = new HashMap<>();
            keycloakUser.put("firstName", userDetails.get("firstName"));
            keycloakUser.put("lastName", userDetails.get("lastName"));
            keycloakUser.put("email", userDetails.get("email"));
            keycloakUser.put("username", userDetails.get("username"));
            keycloakUser.put("enabled", true); // Valor predeterminado
            keycloakUser.put("credentials", new Object[] {
                    Map.of(
                            "type", "password",
                            "value", userDetails.get("password"),
                            "temporary", false
                    )
            });

            keycloakUser.put("attributes", Map.of(
                    "phone_number", userDetails.get("phone_number"),
                    "adress", userDetails.get("adress")
            ));

            String accessToken = keycloakService.getAccessToken();
            keycloakService.createUser(keycloakUser);
            String userId = keycloakService.getUserIdByUsername((String) userDetails.get("username"), accessToken);
            String studentRoleId = "521c8197-2701-4643-8bec-9837dfc1745d"; // ID del rol 'coach'
            keycloakService.assignRoleToUserCoach(userId, studentRoleId, accessToken);

            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error al crear usuario: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
