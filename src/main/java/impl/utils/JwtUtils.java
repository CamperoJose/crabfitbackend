//package impl.utils;
//
//import dao.repositories.PersonRepository;
//import dao.repositories.RoleRepository;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import io.jsonwebtoken.Claims;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class JwtUtils {
//
//    // Define tu clave secreta personalizada como un string
//    private static final String SECRET_KEY_STRING = "MiClaveSuperSecretaMuySeguraYPersonalizada123!";
//    private static final SecretKey SECRET_KEY = new SecretKeySpec(SECRET_KEY_STRING.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//
//    /**
//     * Genera un JWT token para un usuario y sus roles
//     * @param person La persona para quien se genera el token
//     * @param roles Lista de roles asociados a la persona
//     * @return El JWT token
//     */
//    public static String generateToken(PersonRepository person, List<RoleRepository> roles) {
//        return Jwts.builder()
//                .setSubject(person.getUsername())
//                .claim("id", person.id)
//                .claim("name", person.getName())
//                .claim("mail", person.getMail())
//                .claim("roles", roles.stream().map(RoleRepository::getRoleName).collect(Collectors.toList()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
//                .signWith(SECRET_KEY)
//                .compact();
//    }
//
//    /**
//     * Valida si un token es válido y no ha expirado.
//     * @param token El token JWT a validar
//     * @return El objeto Claims del token si es válido, de lo contrario lanza una excepción
//     */
//    public static Claims parseToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /**
//     * Verifica si el token contiene el rol de 'gym_admin'.
//     * @param token El token JWT a verificar
//     * @return true si el token tiene el rol 'gym_admin', false en caso contrario
//     */
//    public static boolean isValidAdminToken(String token) {
//        try {
//            Claims claims = parseToken(token);
//            List<String> roles = claims.get("roles", List.class);
//            //imprimir roles:
//            System.out.println("Roles: " + roles);
//            return roles != null && roles.contains("gym_admin");
//        } catch (Exception e) {
//            System.out.println(e);
//
//            return false;
//        }
//    }
//
//    /**
//     * Extrae el nombre de usuario del token.
//     * @param token El token JWT
//     * @return El nombre de usuario extraído del token
//     */
//    public static String getUsernameFromToken(String token) {
//        return parseToken(token).getSubject();
//    }
//
//    /**
//     * Extrae el ID de la persona desde el token.
//     * @param token El token JWT
//     * @return El ID de la persona extraído del token
//     */
//    public static Long getPersonIdFromToken(String token) {
//        return parseToken(token).get("id", Long.class);
//    }
//
//    /**
//     * Extrae el correo electrónico de la persona desde el token.
//     * @param token El token JWT
//     * @return El correo electrónico de la persona
//     */
//    public static String getMailFromToken(String token) {
//        return parseToken(token).get("mail", String.class);
//    }
//
//    /**
//     * Valida si un token está vencido.
//     * @param token El token JWT
//     * @return true si el token está expirado, false en caso contrario
//     */
//    public static boolean isTokenExpired(String token) {
//        return parseToken(token).getExpiration().before(new Date());
//    }
//}
