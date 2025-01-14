package impl;

import dao.repositories.PersonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import impl.utils.PasswordConstraint;
import impl.utils.PasswordUtils;
import impl.utils.ResponseFormatter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class PersonImpl {

    @Transactional
    public Map<String, String> registerPerson(
            @NotBlank(message = "El nombre no puede estar vacío.") String name,
            @NotBlank(message = "El nombre de usuario no puede estar vacío.") String username,
            @NotBlank(message = "El correo no puede estar vacío.")
            @Email(message = "El formato del correo no es válido.") String mail,
            @PasswordConstraint(message = "La contraseña debe tener al menos 6 caracteres, incluir una mayúscula, una minúscula y un número.") String secret
    ) {
        try {
            // Validar si el username o email ya existen
            if (PersonRepository.find("username", username).firstResult() != null) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
            }
            if (PersonRepository.find("mail", mail).firstResult() != null) {
                throw new IllegalArgumentException("El correo ya está registrado.");
            }

            // Cifrar la contraseña
            String hashedSecret = PasswordUtils.hashPassword(secret);

            // Crear y persistir la entidad
            PersonRepository person = new PersonRepository(name, username, mail, hashedSecret, new Date());
            person.persist();

            // Crear la respuesta con un formato JSON ordenado
            Map<String, String> response = new LinkedHashMap<>();
            response.put("id", person.id.toString());
            response.put("name", person.getName());
            response.put("username", person.getUsername());
            response.put("mail", person.getMail());
            response.put("created_at", person.getCreated_at().toString());

            return response; // Retornar la respuesta en formato Map
        } catch (IllegalArgumentException e) {
            // En caso de error, retornamos el error con mensaje claro
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            // Capturar y devolver errores de validación de manera estandarizada
            throw new IllegalArgumentException("Error al registrar usuario. Detalles: " + e.getMessage());
        }
    }
}
