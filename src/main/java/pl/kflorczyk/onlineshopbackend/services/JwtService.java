package pl.kflorczyk.onlineshopbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kflorczyk.onlineshopbackend.model.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

@Component
public class JwtService {
    private static final String ISSUER = "onlineshop.app";
    private final byte[] secretKey;
    private UserService userService;

    @Autowired
    public JwtService(UserService userService) {
        this.userService = userService;
        this.secretKey = "temporary solution xff".getBytes();
    }

    public String tokenFor(User user) {
//        Date expiration = Date.from(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC));
        Date expiration = DateTime.now(DateTimeZone.UTC).plusHours(2).toDate();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expiration)
                .setIssuer(ISSUER)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public User verify(String token) throws IOException, URISyntaxException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return userService.getUser(claims.getBody().getSubject());
    }
}
