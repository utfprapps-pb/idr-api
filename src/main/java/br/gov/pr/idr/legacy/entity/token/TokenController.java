package br.gov.pr.idr.legacy.entity.token;

import br.gov.pr.idr.legacy.entity.token.dto.UserTokenDto;
import br.gov.pr.idr.legacy.entity.user.User;
import br.gov.pr.idr.legacy.security.AuthenticationResponse;
import br.gov.pr.idr.legacy.security.SecurityConstants;
import br.gov.pr.idr.legacy.utils.GenericResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("tokenAuth")
public class TokenController {

	private final AuthService authService;
	
	 public TokenController(AuthService authService) {
	        this.authService = authService;
	}
	
    @GetMapping
    public GenericResponse checkTokenValidation() {
        return new GenericResponse("Autenticação do Token realizada.");
    }

    @PostMapping("/refreshToken")
    public void refreshToken(@Valid @RequestBody RefreshToken refreshToken, HttpServletResponse response) throws IOException {
        if(!refreshToken.getRefreshToken().substring(0, 7).startsWith(SecurityConstants.TOKEN_PREFIX)) {
            throw new RuntimeException("Token inválido");
        }
        
        User user = (User) authService.loadUserByUsername(refreshToken.getUsername());
        
        String token = JWT.create()
                .withSubject(refreshToken.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(
                new AuthenticationResponse(token, new UserTokenDto(user))
        ));
    }

}
