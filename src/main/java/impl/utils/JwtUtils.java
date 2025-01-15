package impl.utils;

import dao.repositories.PersonRepository;
import dao.repositories.RoleRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;



public class JwtUtils {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(PersonRepository person, List<RoleRepository> roles) {
        return Jwts.builder()
                .setSubject(person.getUsername())
                .claim("id", person.id)
                .claim("name", person.getName())
                .claim("mail", person.getMail())
                .claim("roles", roles.stream().map(RoleRepository::getRoleName).toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(SECRET_KEY)
                .compact();
    }
}
