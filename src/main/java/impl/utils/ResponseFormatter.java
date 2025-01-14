package impl.utils;

import jakarta.ws.rs.core.Response;

import java.util.LinkedHashMap;
import java.util.Map;


    public class ResponseFormatter {

        public static Response badRequest(String internalCode, String message) {
            // Creamos un mapa con el código de error y el mensaje
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("internal-code", internalCode);  // Código de error
            response.put("data", "");  // Devolvemos vacío en los datos
            response.put("message", message);  // El mensaje de error

            // Devolvemos la respuesta con el estado 400 (Bad Request)
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        public static Response success(Map<String, String> data, String message, String code) {
            // Para el caso de éxito, devolvemos el código y mensaje proporcionado
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("internal-code", code);
            response.put("data", data);
            response.put("message", message);

            return Response.status(Response.Status.OK).entity(response).build();
        }

        public static Response error(String details, String internalCode) {
            // Devolvemos la respuesta con el código de error y el detalle en el mensaje
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("internal-code", internalCode);
            response.put("data", "");
            response.put("message", details);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }


}
