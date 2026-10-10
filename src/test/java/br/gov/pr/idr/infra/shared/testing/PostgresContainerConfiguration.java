package br.gov.pr.idr.infra.shared.testing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Throwaway Postgres for integration tests. {@link ServiceConnection} wires the datasource to it, so tests never
 * touch the development database (whose schema is create-drop). Same major version as the dev {@code idr-db}.
 * Spring's test-context cache reuses one container for every test class sharing this configuration.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18-alpine");
    }
}
