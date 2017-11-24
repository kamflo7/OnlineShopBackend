package pl.kflorczyk.onlineshopbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.kflorczyk.onlineshopbackend.jwt_authentication.JwtAuthToken;
import pl.kflorczyk.onlineshopbackend.model.User;
import pl.kflorczyk.onlineshopbackend.repositories.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    private static final String ISSUER = "onlineshop.app";
    private final byte[] secretKey;
    private UserRepository userRepository;

    @Autowired
    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.secretKey = "temporary solution xff".getBytes();
    }

    public String tokenFor(User user) {
        Date expiration = DateTime.now(DateTimeZone.UTC).plusHours(2).toDate();

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("jti", user.getID());
        claims.put("exp", expiration);
        claims.put("iss", ISSUER);
        claims.put("admin", user.isAdmin());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public User verify(String token) throws IOException, URISyntaxException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return userRepository.findByEmail(claims.getBody().getSubject());
    }

    public User verify(JwtAuthToken jwtAuthToken) throws IOException, URISyntaxException {
        return verify(jwtAuthToken.getToken());
    }
}
