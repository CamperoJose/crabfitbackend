//package impl.utils;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.ConstraintViolationException;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.ext.ExceptionMapper;
//import jakarta.ws.rs.ext.Provider;
//import java.util.Set;
//
//@Provider
//public class GlobalExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
//
//    @Override
//    public Response toResponse(ConstraintViolationException exception) {
//        String message = "Ocurriero un error.";
//        String internalCode = "ERR-SET-000";
//        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
//            message = violation.getMessage();
//            internalCode = "ERR-000";
//        }
//
//        return ResponseFormatter.badRequest(internalCode, message);
//    }
//
//    private String formatViolationDetails(Set<ConstraintViolation<?>> violations) {
//        StringBuilder details = new StringBuilder();
//        for (ConstraintViolation<?> violation : violations) {
//            String field = violation.getPropertyPath().toString();
//            String message = violation.getMessage();
//            details.append(field).append(": ").append(message).append("\n");
//        }
//        return details.toString();
//    }
//}
