package br.gov.pr.idr.infra.iam.security;

import br.gov.pr.idr.infra.iam.security.config.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private final JwtProperties jwtProperties;

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
                .sign(Algorithm.HMAC384(jwtProperties.secret()));
    }

    public String extractUsername(String token) {
        try {
            return JWT.require(Algorithm.HMAC384(jwtProperties.secret()))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }
}
