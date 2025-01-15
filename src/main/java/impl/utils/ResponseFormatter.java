package impl.utils;

import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseFormatter {

    // Maneja la respuesta de error
    public static Response badRequest(String internalCode, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("internal-code", internalCode);
        response.put("data", "");
        response.put("message", message);

        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    // Maneja la respuesta exitosa con Map<String, Object> o Map<String, String>
    public static Response success(Map<String, ?> data, String message, String code) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("internal-code", code);
        response.put("data", data);  // Se acepta Map<String, Object> o Map<String, String>
        response.put("message", message);

        return Response.status(Response.Status.OK).entity(response).build();
    }

    // Maneja la respuesta de error del servidor
    public static Response error(String details, String internalCode) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("internal-code", internalCode);
        response.put("data", "");
        response.put("message", details);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
