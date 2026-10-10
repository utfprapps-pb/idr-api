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
 * Integration test of a persistence adapter (JPA repositories, gateways, projections) against a real Postgres in
 * Docker. Tagged {@code integration}: skipped by {@code mvn test}, run on demand with {@code ./mvnw verify -Pit}.
 * Each test runs in a transaction that is rolled back; the schema and the {@code import.sql} seed load once.
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
