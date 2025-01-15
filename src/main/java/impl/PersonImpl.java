package impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dao.repositories.PersonRepository;
import dao.repositories.PersonRoleRepository;
import dao.repositories.RoleRepository;
import impl.utils.JwtUtils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import impl.utils.PasswordConstraint;
import impl.utils.PasswordUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            RoleRepository role = RoleRepository.findById(2);
            PersonRoleRepository personRole = new PersonRoleRepository(person, role);
            personRole.persist();

            return response;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al registrar usuario. Detalles: " + e.getMessage());
        }
    }

    @Transactional
    public Map<String, Object> loginPerson(
            @NotBlank(message = "El nombre de usuario no puede estar vacío.") String username,
            @NotBlank(message = "La contraseña no puede estar vacía.") String secret
    ) {
        try {
            PersonRepository person = PersonRepository.find("username", username).firstResult();

            if (person == null) {
                throw new IllegalArgumentException("Usuario inexistente.");
            }

            if (!PasswordUtils.verifyPassword(secret, person.getSecret())) {
                throw new IllegalArgumentException("Credenciales incorrectas.");
            }

            List<RoleRepository> roles = PersonRoleRepository.find("person", person)
                    .stream()
                    .map(entity -> ((PersonRoleRepository) entity).getRole())
                    .collect(Collectors.toList());

            String token = JwtUtils.generateToken(person, roles);

            Map<String, Object> response = new LinkedHashMap<>();

            response.put("person_id", person.id);
            response.put("name", person.getName());
            response.put("username", person.getUsername());
            response.put("mail", person.getMail());
            response.put("roles", roles.stream().map(RoleRepository::getRoleName).collect(Collectors.toList()));

            response.put("token", token);

            return response;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al iniciar sesión. Detalles: " + e.getMessage());
        }
    }

    @Transactional
    public Map<String, String> registerCoach(
            @NotBlank(message = "El nombre no puede estar vacío.") String name,
            @NotBlank(message = "El nombre de usuario no puede estar vacío.") String username,
            @NotBlank(message = "El correo no puede estar vacío.")
            @Email(message = "El formato del correo no es válido.") String mail,
            @PasswordConstraint(message = "La contraseña debe tener al menos 6 caracteres, incluir una mayúscula, una minúscula y un número.") String secret,
            String token
    ) {
        try {
            // Verificar el rol de admin a través del token

            //imprimior token:
            System.out.println("Token: " + token);

            if (!JwtUtils.isValidAdminToken(token)) {
                throw new IllegalArgumentException("No tiene permisos para registrar un coach.");
            }

            // Validación de usuario existente
            if (PersonRepository.find("username", username).firstResult() != null) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
            }
            if (PersonRepository.find("mail", mail).firstResult() != null) {
                throw new IllegalArgumentException("El correo ya está registrado.");
            }

            // Crear el coach
            String hashedSecret = PasswordUtils.hashPassword(secret);
            PersonRepository person = new PersonRepository(name, username, mail, hashedSecret, new Date());
            person.persist();

            // Asignar rol de 'coach' después de la creación
            RoleRepository role = RoleRepository.findById(1); // ID de 'coach'
            PersonRoleRepository personRole = new PersonRoleRepository(person, role);
            personRole.persist();

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
            throw new IllegalArgumentException("Error al registrar coach. Detalles: " + e.getMessage());
        }
    }


}
