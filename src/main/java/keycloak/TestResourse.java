package keycloak;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/testingkc")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class TestResourse {

    @GET
    @Path("/test01")
    @PermitAll
    public String test01() {
        return "{\"message\": \"Permitido\"}";
    }

    @GET
    @Path("/test02")
    @RolesAllowed("admin")
    public Response test02() {
        // Si no se tiene el rol "admin", devolver un mensaje personalizado
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorResponse("Forbidden", "You do not have the necessary permissions to access this resource."))
                .build();
    }

    // Clase interna para la estructura de la respuesta
    public static class ErrorResponse {
        private String error;
        private String message;

        public ErrorResponse(String error, String message) {
            this.error = error;
            this.message = message;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
