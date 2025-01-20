package keycloak;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Path("/myuser")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOG = Logger.getLogger(UserResource.class);

    @Inject
    KeycloakService keycloakService;

    @POST
    public Response createUser(Map<String, Object> userDetails) {
        LOG.debug("Received request to create user: " + userDetails);

        try {
            // Transformar los datos al formato esperado por Keycloak
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

            // Agregar atributos personalizados
            keycloakUser.put("attributes", Map.of(
                    "phone_number", userDetails.get("phone_number"),
                    "adress", userDetails.get("adress")
            ));

            // Llamar al servicio para crear el usuario en Keycloak
            keycloakService.createUser(keycloakUser);

            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error al crear usuario: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
