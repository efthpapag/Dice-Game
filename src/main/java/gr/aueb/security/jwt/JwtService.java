package gr.aueb.security.jwt;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Singleton;

@Singleton
public class JwtService {

    public String generateJwt(String username) {
        return Jwt.issuer("DiceGame.jwt")
                .subject(username)
                .expiresAt(System.currentTimeMillis()/1000 + 3600).sign();
    }
}
