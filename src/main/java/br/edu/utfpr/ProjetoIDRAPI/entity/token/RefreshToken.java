package br.edu.utfpr.ProjetoIDRAPI.entity.token;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    private String refreshToken;

}
