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
            if (PersonRepository.find("username", username).firstResult() != null) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
            }
            if (PersonRepository.find("mail", mail).firstResult() != null) {
                throw new IllegalArgumentException("El correo ya está registrado.");
            }

            String hashedSecret = PasswordUtils.hashPassword(secret);

            PersonRepository person = new PersonRepository(name, username, mail, hashedSecret, new Date());
            person.persist();

            Map<String, String> response = new LinkedHashMap<>();
            response.put("id", person.id.toString());
            response.put("name", person.getName());
            response.put("username", person.getUsername());
            response.put("mail", person.getMail());
            response.put("created_at", person.getCreated_at().toString());

            return response;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al registrar usuario. Detalles: " + e.getMessage());
        }
    }
}
