package br.gov.pr.idr.architecture;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Guarda de regressão para o defeito CRÍTICO encontrado na revisão final do plano de migração
 * de livestock: duas classes diferentes com o mesmo simple name (ex.: AnimalController em
 * legacy.entity.animal e infra.livestock.api) geram o mesmo nome de bean padrão do Spring
 * ("animalController"), causando ConflictingBeanDefinitionException / BeanDefinitionStoreException
 * na subida da aplicação.
 * <p>
 * Nenhum teste existente no projeto sobe um ApplicationContext real (todos os testes de controller
 * usam MockMvc standalone com dependências mockadas), então esse tipo de problema passava
 * silenciosamente. Este teste faz um scan de classpath leve (sem Spring Boot, sem DataSource)
 * usando o próprio scanner de definição de beans do Spring para garantir que não existam
 * nomes de bean conflitantes nem definições ambíguas entre os componentes anotados do projeto.
 */
@DisplayName("Spring — Conflito de Definição de Beans")
class SpringBeanDefinitionConflictTest {

    private static final String BASE_PACKAGE = "br.gov.pr.idr";

    @Test
    @DisplayName("Scan de @Controller/@RestController/@Service/@Component/@Repository não deve gerar " +
            "BeanDefinitionStoreException (nomes de bean duplicados)")
    void scanningAllStereotypeAnnotatedClassesShouldNotConflict() {
        final var context = new GenericApplicationContext();
        final var scanner = new ClassPathBeanDefinitionScanner(context);

        scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Repository.class));

        assertDoesNotThrow(() -> scanner.scan(BASE_PACKAGE), "O scan de beans não deveria lançar " +
                "BeanDefinitionStoreException/ConflictingBeanDefinitionException; isso indica duas classes " +
                "com o mesmo simple name (ex.: AnimalController) resolvendo para o mesmo nome de bean.");
    }

    @Test
    @DisplayName("Não deve haver BeanDefinitionStoreException explicitamente ao registrar os beans no contexto")
    void registeringBeansInContextShouldNotThrowConflictingBeanDefinition() {
        final var context = new GenericApplicationContext();
        final var scanner = new ClassPathBeanDefinitionScanner(context);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Service.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Component.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Repository.class));

        try {
            final int registered = scanner.scan(BASE_PACKAGE);
            org.junit.jupiter.api.Assertions.assertTrue(registered > 0,
                    "Esperado registrar ao menos um bean candidato no pacote " + BASE_PACKAGE);
        } catch (final BeanDefinitionStoreException ex) {
            org.junit.jupiter.api.Assertions.fail(
                    "Conflito de definição de bean detectado (nomes de bean duplicados): " + ex.getMessage(), ex);
        }
    }
}
