package rest;

import dto.UserRequestDTO;
import impl.utils.keycloak.KeycloakService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.logging.Logger;

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
    public Response createStudentUser(UserRequestDTO userRequest) {
        try {
            keycloakService.createAndAssignRole(userRequest, "student");
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error al crear usuario estudiante: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/coach")
    @RolesAllowed("admin")
    public Response createCoachUser(UserRequestDTO userRequest) {
        try {
            keycloakService.createAndAssignRole(userRequest, "coach");
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error al crear usuario coach: ", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}
