package br.gov.pr.idr.domain.shared.tactical;

import br.gov.pr.idr.domain.iam.user.vo.CPF;

import java.lang.annotation.*;
/**
 * Considere utilizar ValueObject quando:
 * Seu próprio valor é seu identificador único
 * Seu valor é imutável
 * Um V.O deve se auto validar ex: {@link CPF}
 * Boa prática: Utilize de records para representar ValueObjects
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ValueObject {
}
