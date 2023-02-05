package it.polimi.emall.emsp.customerservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.polimi.emall.emsp.customerservice.model.CustomerTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtHelper {
    private final String secret;
    private final String key;

    public JwtHelper(
            @Value("${authentication.secret}") String secret,
            @Value("${authentication.key}") String key
    ) {
        this.secret = secret;
        this.key = key;
    }

    public String generateToken(Long customerId){
        return JWT.create()
                .withClaim("key", key)
                .withClaim("customerId", customerId)
                .withExpiresAt(Instant.now().plus(365, ChronoUnit.DAYS))
                .sign(Algorithm.HMAC256(secret));
    }

    public CustomerTokenDto buildTokenDto(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return new CustomerTokenDto(
                decodedJWT.getClaim("key").asString(),
                decodedJWT.getClaim("customerId").asLong()
        );
    }

    public CustomerTokenDto buildTokenDtoFromRequest(){
        return buildTokenDto(HttpRequestUtils.getAuthorizationToken());
    }
}
