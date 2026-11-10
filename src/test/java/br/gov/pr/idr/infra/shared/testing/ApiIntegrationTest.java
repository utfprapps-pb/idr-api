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
 * Teste de API ponta a ponta: a aplicação inteira (filtros de segurança, controllers, use cases, gateways) via
 * HTTP contra um Postgres real em Docker, populado pelo {@code import.sql}. Tag {@code integration}: ignorado no
 * {@code mvn test}, roda sob demanda com {@code ./mvnw verify -Pit}. Cada teste roda numa transação desfeita ao
 * final. O armazenamento de objetos é mockado, então o contexto sobe sem MinIO. Estenda {@link ApiTestSupport}
 * para ter o cliente autenticado.
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
