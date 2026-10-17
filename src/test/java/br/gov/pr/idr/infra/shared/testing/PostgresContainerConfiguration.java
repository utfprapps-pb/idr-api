package br.gov.pr.idr.infra.shared.testing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Postgres descartável para os testes de integração. O {@link ServiceConnection} liga o datasource a ele, então os
 * testes nunca tocam o banco de desenvolvimento (cujo schema é create-drop). Mesma versão principal do
 * {@code idr-db} de dev. O cache de contexto de teste do Spring reaproveita um único container para todas as
 * classes de teste que compartilham esta configuração.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18-alpine");
    }
}
