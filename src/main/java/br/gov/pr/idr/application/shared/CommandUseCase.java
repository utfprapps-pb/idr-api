package br.gov.pr.idr.application.shared;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Um caso de uso serve para designar um comportamento entre a camada de infraestrutra e as regras do domínio
 * O que deve conter: Regras de integração de outros bounded contexts e/ou dominios/sub-dominios.
 * OBS: Regras da entidade não devem ser "vazadas" para o caso de uso
*/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@Documented
@Service
public @interface CommandUseCase {
}
