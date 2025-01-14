package rest;

import dao.repositories.PersonRepository;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import impl.PersonImpl;
import impl.utils.ResponseFormatter;

import java.util.Map;

@Path("/api/v1/person")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonRest {

    @Inject
    PersonImpl personService;

    @POST
    @Path("/register")
    public Response registerPerson(PersonRepository personRequest) {
        try {
            Map<String, String> response = personService.registerPerson(
                    personRequest.getName(),
                    personRequest.getUsername(),
                    personRequest.getMail(),
                    personRequest.getSecret()
            );

            return ResponseFormatter.success(response, "Solicitud exitosa y persona registrada correctamente", "INS-001");

        } catch (IllegalArgumentException e) {
            // En caso de error, devolvemos un código interno y el mensaje de error
            return ResponseFormatter.badRequest("ERR-002", e.getMessage());

        } catch (ConstraintViolationException e) {
            // Para las violaciones de restricciones, extraemos el mensaje y asignamos un código
            String message = "Ocurrieron errores de validación.";
            String internalCode = "ERR-003";
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                message = violation.getMessage();  // Asignamos el mensaje de la violación
            }
            return ResponseFormatter.badRequest(internalCode, message);

        } catch (Exception e) {
            // Error general, devolvemos un código de error interno y el mensaje
            return ResponseFormatter.error(e.getMessage(), "ERR-001");
        }
    }


}
