package br.gov.pr.idr.infra.shared.persistence;

import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Hibernate loads {@code import.sql} at startup and only logs a statement that fails, so a broken seed row
 * (a renamed column, a missing NOT NULL value) goes unnoticed. This checks every seeded table holds exactly as
 * many rows as the seed inserts into it.
 */
@PersistenceAdapterTest
@DisplayName("import.sql — seed carregado por completo")
class SeedDataDatabaseTest {

    private static final Pattern INSERT = Pattern.compile("(?im)^\\s*insert\\s+into\\s+(\\w+)");

    @Autowired TestEntityManager entityManager;

    @Test
    @DisplayName("cada tabela do seed tem uma linha por insert do import.sql")
    void everySeedStatementShouldHaveBeenApplied() throws IOException {
        final var seed = new ClassPathResource("import.sql").getContentAsString(StandardCharsets.UTF_8);
        final Map<String, Long> expected = new TreeMap<>();
        INSERT.matcher(seed).results().forEach(m -> expected.merge(m.group(1).toLowerCase(), 1L, Long::sum));

        assertFalse(expected.isEmpty(), "no insert statement found in import.sql");

        final Map<String, Long> actual = new TreeMap<>();
        expected.keySet().forEach(table -> actual.put(table, ((Number) entityManager.getEntityManager()
                .createNativeQuery("select count(*) from " + table).getSingleResult()).longValue()));

        assertEquals(expected, actual);
    }
}
