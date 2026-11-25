package br.gov.pr.idr.infra.livestock;

import br.gov.pr.idr.domain.inventory_resource.medication.Medication;
import br.gov.pr.idr.domain.livestock.animal.AnimalID;
import br.gov.pr.idr.domain.livestock.animal_childbirth.AnimalChildbirth;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthCondition;
import br.gov.pr.idr.domain.livestock.animal_childbirth.vo.ChildbirthGender;
import br.gov.pr.idr.domain.livestock.animal_death.AnimalDeath;
import br.gov.pr.idr.domain.livestock.animal_diseases.AnimalDiseases;
import br.gov.pr.idr.domain.livestock.animal_purchases.AnimalPurchases;
import br.gov.pr.idr.domain.livestock.animal_sales.AnimalSales;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Destination;
import br.gov.pr.idr.domain.livestock.animal_sales.vo.Reason;
import br.gov.pr.idr.domain.livestock.breed.BreedID;
import br.gov.pr.idr.domain.livestock.insemination.Insemination;
import br.gov.pr.idr.domain.livestock.mastitis.Mastitis;
import br.gov.pr.idr.domain.livestock.pregnancy_diagnose.PregnancyDiagnose;
import br.gov.pr.idr.domain.shared.tactical.search.Pagination;
import br.gov.pr.idr.domain.shared.tactical.search.SearchQuery;
import br.gov.pr.idr.infra.inventory_resource.medication.MedicationPostgresGateway;
import br.gov.pr.idr.infra.inventory_resource.medication.persistence.MedicationJPARepository;
import br.gov.pr.idr.infra.livestock.animal_childbirth.AnimalChildbirthPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal_childbirth.persistence.AnimalChildbirthJPARepository;
import br.gov.pr.idr.infra.livestock.animal_death.AnimalDeathPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal_death.persistence.AnimalDeathJPARepository;
import br.gov.pr.idr.infra.livestock.animal_diseases.AnimalDiseasesPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal_diseases.persistence.AnimalDiseasesJPARepository;
import br.gov.pr.idr.infra.livestock.animal_purchases.AnimalPurchasesPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal_purchases.persistence.AnimalPurchasesJPARepository;
import br.gov.pr.idr.infra.livestock.animal_sales.AnimalSalesPostgresGateway;
import br.gov.pr.idr.infra.livestock.animal_sales.persistence.AnimalSalesJPARepository;
import br.gov.pr.idr.infra.livestock.insemination.InseminationPostgresGateway;
import br.gov.pr.idr.infra.livestock.insemination.persistence.InseminationJPARepository;
import br.gov.pr.idr.infra.livestock.mastitis.MastitisPostgresGateway;
import br.gov.pr.idr.infra.livestock.mastitis.persistence.MastitisJPARepository;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.PregnancyDiagnosePostgresGateway;
import br.gov.pr.idr.infra.livestock.pregnancy_diagnose.persistence.PregnancyDiagnoseJPARepository;
import br.gov.pr.idr.infra.shared.testing.PersistenceAdapterTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Busca dos eventos de um animal com {@code terms}: uma data ISO filtra pela data própria do evento; texto que não é
 * data mantém a listagem de todos os eventos do animal.
 */
@PersistenceAdapterTest
@DisplayName("Busca dos eventos do animal por data — Postgres real")
class AnimalEventSearchByDateDatabaseTest {

    private static final LocalDate MARCH = LocalDate.of(2024, 3, 10);
    private static final LocalDate APRIL = LocalDate.of(2024, 4, 20);

    @Autowired TestEntityManager entityManager;
    @Autowired InseminationJPARepository inseminations;
    @Autowired MastitisJPARepository mastitides;
    @Autowired PregnancyDiagnoseJPARepository pregnancyDiagnoses;
    @Autowired AnimalDiseasesJPARepository diseases;
    @Autowired AnimalPurchasesJPARepository purchases;
    @Autowired AnimalSalesJPARepository sales;
    @Autowired MedicationJPARepository medications;
    @Autowired AnimalChildbirthJPARepository childbirths;
    @Autowired AnimalDeathJPARepository deaths;

    @Test
    @DisplayName("inseminações: filtra por iaDate")
    void shouldFilterInseminationsByDate() {
        final var gateway = new InseminationPostgresGateway(inseminations);
        assertFiltersByDate(date -> animalId -> gateway.save(Insemination.create(animalId, date, "Touro")),
                gateway::search, Insemination::getIaDate, "iaDate");
    }

    @Test
    @DisplayName("mastites: filtra por diagnoseDate")
    void shouldFilterMastitidesByDate() {
        final var gateway = new MastitisPostgresGateway(mastitides);
        assertFiltersByDate(date -> animalId -> gateway.save(Mastitis.create(animalId, date, "Clinica", "+",
                        "Positivo", "Negativo", "Positivo", "Negativo")),
                gateway::search, Mastitis::getDiagnoseDate, "diagnoseDate");
    }

    @Test
    @DisplayName("diagnósticos de gestação: filtra por diagnoseDate, não por lastIa")
    void shouldFilterPregnancyDiagnosesByDate() {
        final var gateway = new PregnancyDiagnosePostgresGateway(pregnancyDiagnoses);
        // lastIa igual em todos: se o filtro usasse lastIa, a busca por diagnoseDate não devolveria nada.
        assertFiltersByDate(date -> animalId -> gateway.save(PregnancyDiagnose.create(animalId, date,
                        LocalDate.of(2024, 1, 5))),
                gateway::search, PregnancyDiagnose::getDiagnoseDate, "diagnoseDate");
    }

    @Test
    @DisplayName("doenças: filtra por diagnosisDate")
    void shouldFilterDiseasesByDate() {
        final var gateway = new AnimalDiseasesPostgresGateway(diseases);
        assertFiltersByDate(date -> animalId -> gateway.save(AnimalDiseases.create(animalId, "Pneumonia", date)),
                gateway::search, AnimalDiseases::getDiagnosisDate, "diagnosisDate");
    }

    @Test
    @DisplayName("compras: filtra por datePurchase")
    void shouldFilterPurchasesByDate() {
        final var gateway = new AnimalPurchasesPostgresGateway(purchases);
        assertFiltersByDate(date -> animalId -> gateway.save(AnimalPurchases.create(animalId, date,
                        LocalDate.of(2020, 1, 1), BigDecimal.valueOf(1500), BigDecimal.valueOf(450))),
                gateway::search, AnimalPurchases::getDatePurchase, "datePurchase");
    }

    @Test
    @DisplayName("vendas: filtra por dateSale")
    void shouldFilterSalesByDate() {
        final var gateway = new AnimalSalesPostgresGateway(sales);
        assertFiltersByDate(date -> animalId -> gateway.save(AnimalSales.create(animalId, date, Reason.VOLUNTARIA,
                        BigDecimal.valueOf(1500), Destination.ABATE, BigDecimal.valueOf(450))),
                gateway::search, AnimalSales::getDateSale, "dateSale");
    }

    @Test
    @DisplayName("medicações: filtra por applicationDate")
    void shouldFilterMedicationsByDate() {
        final var gateway = new MedicationPostgresGateway(medications);
        assertFiltersByDate(date -> animalId -> gateway.save(Medication.create(animalId, null, "Ivomec",
                        "Ivermectina", date, "10 ml", "Intramuscular")),
                gateway::search, Medication::getApplicationDate, "applicationDate");
    }

    @Test
    @DisplayName("partos: filtra por childbirthDate")
    void shouldFilterChildbirthsByDate() {
        final var gateway = new AnimalChildbirthPostgresGateway(childbirths);
        assertFiltersByDate(date -> animalId -> gateway.save(AnimalChildbirth.create(animalId, date,
                        ChildbirthGender.MALE, new BigDecimal("38.40"), ChildbirthCondition.DEAD, BreedID.unique(),
                        AnimalID.unique())),
                gateway::search, AnimalChildbirth::getChildbirthDate, "childbirthDate");
    }

    @Test
    @DisplayName("morte (uma por animal): filtra por deathDate")
    void shouldFilterDeathByDate() {
        final var gateway = new AnimalDeathPostgresGateway(deaths);
        final var animalId = AnimalID.unique();
        gateway.save(AnimalDeath.create(animalId, "Pneumonia", MARCH));
        gateway.save(AnimalDeath.create(AnimalID.unique(), "Outro animal", MARCH));
        entityManager.flush();

        final var sameDate = gateway.search(animalId, query("2024-03-10", "deathDate"));
        assertEquals(1, sameDate.total());
        assertEquals(MARCH, sameDate.items().get(0).getDeathDate());
        assertEquals(0, gateway.search(animalId, query("2024-04-20", "deathDate")).total());
        assertEquals(1, gateway.search(animalId, query("Pneumonia", "deathDate")).total());
    }

    /**
     * Grava dois eventos do animal (março e abril) e um de outro animal em março; a data de março devolve só o evento
     * de março do animal, e {@code terms} vazio ou que não é data devolve os dois.
     */
    private <T> void assertFiltersByDate(final Function<LocalDate, Function<AnimalID, T>> saveOn,
                                         final BiFunction<AnimalID, SearchQuery, Pagination<T>> search,
                                         final Function<T, LocalDate> eventDate, final String sort) {
        final var animalId = AnimalID.unique();
        saveOn.apply(MARCH).apply(animalId);
        saveOn.apply(APRIL).apply(animalId);
        saveOn.apply(MARCH).apply(AnimalID.unique());
        entityManager.flush();

        final var sameDate = search.apply(animalId, query("2024-03-10", sort));
        assertEquals(1, sameDate.total());
        assertEquals(MARCH, eventDate.apply(sameDate.items().get(0)));

        assertEquals(0, search.apply(animalId, query("2024-05-01", sort)).total());
        assertEquals(2, search.apply(animalId, query("texto livre", sort)).total());
        assertEquals(2, search.apply(animalId, query("", sort)).total());
    }

    private static SearchQuery query(final String terms, final String sort) {
        return SearchQuery.from(0, 10, terms, sort, "asc");
    }
}
