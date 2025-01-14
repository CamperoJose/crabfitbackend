package impl.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Aquí, en lugar de concatenar todo, solo extraemos el mensaje de la violación y el código de error
        String message = "Ocurrieron errores de validación.";
        String internalCode = "ERR-003";  // Esto podría depender del tipo de error o una constante predefinida
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            message = violation.getMessage(); // Aquí asignamos el mensaje de error relevante
            internalCode = "ERR-003"; // Código de error, puedes ajustar según la violación
        }

        // Llamamos a ResponseFormatter con los valores adecuados
        return ResponseFormatter.badRequest(internalCode, message);
    }



    private String formatViolationDetails(Set<ConstraintViolation<?>> violations) {
        StringBuilder details = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            details.append(field).append(": ").append(message).append("\n");
        }
        return details.toString();
    }

}
