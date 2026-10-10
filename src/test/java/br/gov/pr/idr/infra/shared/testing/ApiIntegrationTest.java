package br.gov.pr.idr.infra.shared.testing;

import br.gov.pr.idr.domain.shared.storage.StorageGateway;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * End-to-end API test: the whole application (security filters, controllers, use cases, gateways) over HTTP against
 * a real Postgres in Docker, seeded by {@code import.sql}. Tagged {@code integration}: skipped by {@code mvn test},
 * run on demand with {@code ./mvnw verify -Pit}. Each test runs in a transaction that is rolled back. Object storage
 * is mocked so the context starts without MinIO. Extend {@link ApiTestSupport} for the authenticated client.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("integration")
@SpringBootTest(properties = "spring.jpa.show-sql=false")
@Transactional
@Import(PostgresContainerConfiguration.class)
@MockitoBean(types = StorageGateway.class)
public @interface ApiIntegrationTest {
}
