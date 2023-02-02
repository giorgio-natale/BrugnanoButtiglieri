package it.polimi.emall.cpms.employeeservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.polimi.emall.cpms.employeeservice.model.EmployeeTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String generateToken(Long employeeId){
        return JWT.create()
                .withClaim("key", key)
                .withClaim("employeeId", employeeId)
                .sign(Algorithm.HMAC256(secret));
    }

    public EmployeeTokenDto buildTokenDto(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return new EmployeeTokenDto(
                decodedJWT.getClaim("key").asString(),
                decodedJWT.getClaim("employeeId").asLong()
        );
    }

    public EmployeeTokenDto buildTokenDtoFromRequest(){
        return buildTokenDto(HttpRequestUtils.getAuthorizationToken());
    }
}
