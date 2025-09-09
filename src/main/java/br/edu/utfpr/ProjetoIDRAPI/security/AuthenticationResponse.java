package br.edu.utfpr.ProjetoIDRAPI.security;

import br.edu.utfpr.ProjetoIDRAPI.entity.token.dto.UserTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private UserTokenDto user;
}
