package it.polimi.emall.emsp.bookingmanagementservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
    public static class CustomerTokenDto{
        public final String key;
        public final Long customerId;

        @JsonCreator
        public CustomerTokenDto(String key, Long customerId) {
            this.key = key;
            this.customerId = customerId;
        }
    }
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
