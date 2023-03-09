package br.edu.utfpr.ProjetoIDRAPI.controller;

import br.edu.utfpr.ProjetoIDRAPI.utils.GenericResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("tokenAuth")
public class TokenController {

    @GetMapping
    public GenericResponse checkTokenValidation() {
        return new GenericResponse("Autenticação do Token realizada.");
    }

    @GetMapping("/refreshToken")
    public void refreshToken() {

    }

}
