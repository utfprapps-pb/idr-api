package br.gov.pr.idr.architecture;

import br.gov.pr.idr.application.shared.CommandUseCase;
import br.gov.pr.idr.application.shared.QueryUseCase;
import br.gov.pr.idr.application.shared.UseCase;
import br.gov.pr.idr.application.shared.VoidUseCase;
import br.gov.pr.idr.domain.shared.ValueObject;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Testes Arquiteturais — Clean Architecture")
class ArchitectureTest {

    private static final String BASE_PACKAGE = "br.gov.pr.idr";

    private static final String DOMAIN      = "..domain..";
    private static final String APPLICATION = "..application..";
    private static final String INFRA       = "..infra..";
    private static final String LEGACY      = "..legacy..";

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    // =========================================================================
    // Dependências entre camadas
    // =========================================================================

    @Nested
    @DisplayName("Regras de Dependência entre Camadas")
    class LayerDependencyTest {

        @Test
        @DisplayName("Domain não deve depender de Application")
        void domainShouldNotDependOnApplication() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(APPLICATION)
                    .because("A camada Domain é o núcleo da aplicação e não deve conhecer a camada Application")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve depender de Infra")
        void domainShouldNotDependOnInfra() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(INFRA)
                    .because("A camada Domain não deve conhecer detalhes de infraestrutura")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve depender de Legacy")
        void domainShouldNotDependOnLegacy() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage(LEGACY)
                    .because("A camada Domain não deve depender de código legado")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Application não deve depender de Infra")
        void applicationShouldNotDependOnInfra() {
            noClasses()
                    .that().resideInAPackage(APPLICATION)
                    .should().dependOnClassesThat().resideInAPackage(INFRA)
                    .because("A camada Application não deve conhecer detalhes de infraestrutura; " +
                             "use interfaces (Gateways) definidas no Domain para inversão de dependência")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Application não deve depender de Legacy")
        void applicationShouldNotDependOnLegacy() {
            noClasses()
                    .that().resideInAPackage(APPLICATION)
                    .should().dependOnClassesThat().resideInAPackage(LEGACY)
                    .because("A camada Application não deve depender de código legado")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Infra não deve depender de Legacy")
        void infraShouldNotDependOnLegacy() {
            noClasses()
                    .that().resideInAPackage(INFRA)
                    .should().dependOnClassesThat().resideInAPackage(LEGACY)
                    .because("A camada Infra não deve depender de código legado")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Isolamento do Domain
    // =========================================================================

    @Nested
    @DisplayName("Anotações Spring no Domain")
    class SpringAnnotationsInDomainTest {

        @Test
        @DisplayName("Domain não deve usar @Service")
        void domainShouldNotUseServiceAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("org.springframework.stereotype.Service")
                    .because("A camada Domain deve ser agnóstica ao framework")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve usar @Component")
        void domainShouldNotUseComponentAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("org.springframework.stereotype.Component")
                    .because("A camada Domain deve ser agnóstica ao framework")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve usar @Repository")
        void domainShouldNotUseRepositoryAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("org.springframework.stereotype.Repository")
                    .because("A camada Domain deve ser agnóstica ao framework")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve usar @Autowired")
        void domainShouldNotUseAutowiredAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("org.springframework.beans.factory.annotation.Autowired")
                    .because("A camada Domain deve usar injeção via construtor sem anotações Spring")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve depender do Spring Framework")
        void domainShouldNotDependOnSpring() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAPackage("org.springframework..")
                    .because("A camada Domain deve ser agnóstica ao framework")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Isolamento do Domain — JPA / Persistência
    // =========================================================================

    @Nested
    @DisplayName("Anotações JPA no Domain")
    class JPAAnnotationsInDomainTest {

        @Test
        @DisplayName("Domain não deve usar @Entity")
        void domainShouldNotUseEntityAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("jakarta.persistence.Entity")
                    .because("Entidades JPA pertencem à Infra; entidades de domínio são POJOs puros")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve usar @Table")
        void domainShouldNotUseTableAnnotation() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().beAnnotatedWith("jakarta.persistence.Table")
                    .because("A camada Domain deve ser agnóstica ao banco de dados")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Domain não deve depender de jakarta.persistence ou Hibernate")
        void domainShouldNotDependOnJPAOrHibernate() {
            noClasses()
                    .that().resideInAPackage(DOMAIN)
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "jakarta.persistence..",
                            "org.hibernate.."
                    )
                    .because("A camada Domain deve ser agnóstica ao framework de persistência")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Isolamento do Application — Web / Persistência
    // =========================================================================

    @Nested
    @DisplayName("Isolamento da Camada Application")
    class ApplicationLayerIsolationTest {

        @Test
        @DisplayName("Application não deve depender de Spring Web")
        void applicationShouldNotDependOnSpringWeb() {
            noClasses()
                    .that().resideInAPackage(APPLICATION)
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework.web..",
                            "org.springframework.http.."
                    )
                    .because("A camada Application deve ser agnóstica ao protocolo de transporte")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Application não deve depender de jakarta.persistence ou Hibernate")
        void applicationShouldNotDependOnJPAOrHibernate() {
            noClasses()
                    .that().resideInAPackage(APPLICATION)
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "jakarta.persistence..",
                            "org.hibernate.."
                    )
                    .because("A camada Application deve ser agnóstica ao framework de persistência")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Use Cases
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Use Cases")
    class UseCaseConventionsTest {

        @Test
        @DisplayName("Classes concretas em Application devem terminar com 'UseCase', 'Command' ou 'Output'")
        void concreteApplicationClassesShouldFollowNamingConvention() {
            classes()
                    .that().resideInAPackage(APPLICATION)
                    .and().areNotInterfaces()
                    .and().areNotEnums()
                    .and().areNotAnnotations()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .and().areNotMemberClasses()
                    .should().haveSimpleNameEndingWith("UseCase")
                    .orShould().haveSimpleNameEndingWith("Command")
                    .orShould().haveSimpleNameEndingWith("Output")
                    .because("Classes concretas na camada Application devem seguir a convenção de nomenclatura")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Use Cases concretos devem ser anotados com @CommandUseCase ou @QueryUseCase")
        void concreteUseCasesShouldBeAnnotated() {
            classes()
                    .that().resideInAPackage(APPLICATION)
                    .and().haveSimpleNameEndingWith("UseCase")
                    .and().areNotInterfaces()
                    .and().areNotAnnotations()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .should().beAnnotatedWith(CommandUseCase.class)
                    .orShould().beAnnotatedWith(QueryUseCase.class)
                    .because("Use cases concretos devem ser marcados com @CommandUseCase (escrita) ou @QueryUseCase (leitura)")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Use Cases concretos devem estender UseCase ou VoidUseCase")
        void concreteUseCasesShouldExtendBaseClass() {
            classes()
                    .that().resideInAPackage(APPLICATION)
                    .and().haveSimpleNameEndingWith("UseCase")
                    .and().areNotInterfaces()
                    .and().areNotAnnotations()
                    .and().doNotHaveModifier(JavaModifier.ABSTRACT)
                    .should().beAssignableTo(UseCase.class)
                    .orShould().beAssignableTo(VoidUseCase.class)
                    .because("Use cases devem herdar de UseCase<I,O> ou VoidUseCase<I> para padronizar a assinatura")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Value Objects
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Value Objects")
    class ValueObjectConventionsTest {

        @Test
        @DisplayName("Classes em domain..vo.. devem ser anotadas com @ValueObject")
        void voClassesShouldBeAnnotatedWithValueObject() {
            classes()
                    .that().resideInAPackage("..domain..vo..")
                    .and().areNotInterfaces()
                    .should().beAnnotatedWith(ValueObject.class)
                    .because("Value Objects devem ser explicitamente marcados com @ValueObject")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Value Objects devem ser imutáveis (records, classes final ou enums)")
        void voClassesShouldBeImmutable() {
            classes()
                    .that().resideInAPackage("..domain..vo..")
                    .and().areNotInterfaces()
                    .should().beRecords()
                    .orShould().haveModifier(JavaModifier.FINAL)
                    .orShould().beAssignableTo(Enum.class)
                    .because("Value Objects devem ser imutáveis por natureza")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Gateways
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Gateways")
    class GatewayConventionsTest {

        @Test
        @DisplayName("Interfaces no Domain devem terminar com 'Gateway'")
        void domainInterfacesShouldEndWithGateway() {
            classes()
                    .that().resideInAPackage(DOMAIN)
                    .and().areInterfaces()
                    .should().haveSimpleNameEndingWith("Gateway")
                    .because("Interfaces de domínio representam portas de saída (ports) e devem seguir o sufixo 'Gateway'")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Implementações de Gateway devem estar na camada Infra")
        void gatewayImplementationsShouldBeInInfra() {
            classes()
                    .that().haveSimpleNameEndingWith("Gateway")
                    .and().areNotInterfaces()
                    .should().resideInAPackage(INFRA)
                    .because("Implementações de Gateway são adaptadores e pertencem à camada Infra")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Exceções de Domínio
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Exceptions no Domain")
    class DomainExceptionConventionsTest {

        @Test
        @DisplayName("Classes em domain..exceptions.. devem terminar com 'Exception'")
        void domainExceptionsShouldEndWithException() {
            classes()
                    .that().resideInAPackage("..domain..exceptions..")
                    .and().areNotInterfaces()
                    .should().haveSimpleNameEndingWith("Exception")
                    .because("Classes de exceção devem seguir a convenção de nomenclatura com sufixo 'Exception'")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Entidades JPA
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Entidades JPA")
    class JPAEntityConventionsTest {

        @Test
        @DisplayName("Entidades JPA (@Entity) devem estar em infra..persistence..")
        void jpaEntitiesShouldBeInPersistencePackage() {
            classes()
                    .that().areAnnotatedWith("jakarta.persistence.Entity")
                    .should().resideInAPackage("..infra..persistence..")
                    .because("Entidades JPA são detalhes de infraestrutura e devem ficar no pacote de persistência")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("Entidades JPA devem terminar com 'JPAEntity'")
        void jpaEntitiesShouldFollowNamingConvention() {
            classes()
                    .that().areAnnotatedWith("jakarta.persistence.Entity")
                    .should().haveSimpleNameEndingWith("JPAEntity")
                    .because("Entidades JPA devem ser diferenciadas das entidades de domínio pelo sufixo 'JPAEntity'")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }

    // =========================================================================
    // Controllers REST
    // =========================================================================

    @Nested
    @DisplayName("Convenções de Controllers REST")
    class RestControllerConventionsTest {

        @Test
        @DisplayName("@RestController deve estar em infra..api..")
        void restControllersShouldBeInApiPackage() {
            classes()
                    .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                    .should().resideInAPackage("..infra..api..")
                    .because("Controllers REST são adaptadores de entrada e pertencem à camada Infra")
                    .allowEmptyShould(true)
                    .check(classes);
        }

        @Test
        @DisplayName("@RestController deve terminar com 'Controller'")
        void restControllersShouldEndWithController() {
            classes()
                    .that().areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
                    .should().haveSimpleNameEndingWith("Controller")
                    .because("Controllers REST devem seguir a convenção de nomenclatura com sufixo 'Controller'")
                    .allowEmptyShould(true)
                    .check(classes);
        }
    }
}
