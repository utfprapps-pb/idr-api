package br.edu.utfpr.ProjetoIDRAPI.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@Profile("test")
public class ApplicationNoSecurity {

    //Essa classe tem a função de retirar as medidas de segurança no profile de testes.
    //Assim os testes podem ocorrer sem ter que se gerar um Token nas requisições de teste.

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/**");
    }

}
