package keycloak;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

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
    public String test02() {
        return "{\"message\": \"Permitido solo admin\"}";
    }
}
