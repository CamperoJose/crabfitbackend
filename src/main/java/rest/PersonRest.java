//package rest;
//
//import dao.repositories.PersonRepository;
//import jakarta.inject.Inject;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.ConstraintViolationException;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import impl.PersonImpl;
//import impl.utils.ResponseFormatter;
//
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Path("/api/v1/person")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//public class PersonRest {
//
//    @Inject
//    PersonImpl personService;
//
//    @POST
//    @Path("/register")
//    public Response registerPerson(PersonRepository personRequest) {
//        try {
//            Map<String, String> response = personService.registerPerson(
//                    personRequest.getName(),
//                    personRequest.getUsername(),
//                    personRequest.getMail(),
//                    personRequest.getSecret()
//            );
//
//            return ResponseFormatter.success(response, "Solicitud exitosa y persona registrada correctamente", "NEW-USER-001");
//
//        } catch (IllegalArgumentException e) {
//            return ResponseFormatter.badRequest("ERR-USER-055", e.getMessage());
//
//        } catch (ConstraintViolationException e) {
//            String message = "Ocurrieron errores de validación."; // Default message
//            String internalCode = "ERR-USER-044";
//            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//                message = violation.getMessage();
//            }
//            return ResponseFormatter.badRequest(internalCode, message);
//
//        } catch (Exception e) {
//            return ResponseFormatter.error(e.getMessage(), "ERR-099");
//        }
//    }
//
//    @POST
//    @Path("/login")
//    public Response loginPerson(PersonRepository personRequest) {
//        try {
//            Map<String, Object> response = personService.loginPerson(
//                    personRequest.getUsername(),
//                    personRequest.getSecret()
//            );
//
//            return ResponseFormatter.success(response, "Inicio de sesión exitoso", "LOGIN-USER-001");
//
//        } catch (IllegalArgumentException e) {
//            return ResponseFormatter.badRequest("ERR-USER-056", e.getMessage());
//
//        } catch (ConstraintViolationException e) {
//            String message = "Ocurrieron errores de validación."; // Default message
//            String internalCode = "ERR-USER-045";
//            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
//                message = violation.getMessage();
//            }
//            return ResponseFormatter.badRequest(internalCode, message);
//
//        } catch (Exception e) {
//            return ResponseFormatter.error(e.getMessage(), "ERR-099");
//        }
//    }
//
//    @POST
//    @Path("/register/coach")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response registerCoach(PersonRepository personRequest, @HeaderParam("Authorization") String token) {
//        try {
//            Map<String, String> response = personService.registerCoach(
//                    personRequest.getName(),
//                    personRequest.getUsername(),
//                    personRequest.getMail(),
//                    personRequest.getSecret(),
//                    token
//            );
//
//            return ResponseFormatter.success(response, "Coach registrado correctamente", "NEW-COACH-001");
//
//        } catch (IllegalArgumentException e) {
//            return ResponseFormatter.badRequest("ERR-COACH-001", e.getMessage());
//
//        } catch (Exception e) {
//            return ResponseFormatter.error(e.getMessage(), "ERR-099");
//        }
//    }
//
//
//
//
//}
