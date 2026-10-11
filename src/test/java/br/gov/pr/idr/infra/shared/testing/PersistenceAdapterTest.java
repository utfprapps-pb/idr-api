package br.gov.pr.idr.infra.shared.testing;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Teste de integração de um adaptador de persistência (repositórios JPA, gateways, projeções) contra um Postgres
 * real em Docker. Tag {@code integration}: ignorado no {@code mvn test}, roda sob demanda com
 * {@code ./mvnw verify -Pit}. Cada teste roda numa transação desfeita ao final; o schema e o seed do
 * {@code import.sql} carregam uma vez só.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("integration")
@DataJpaTest(properties = "spring.jpa.show-sql=false")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresContainerConfiguration.class)
public @interface PersistenceAdapterTest {
}
