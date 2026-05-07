package br.gov.pr.idr.legacy.entity.insemination;

import br.gov.pr.idr.legacy.entity.animal.Animal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Insemination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate iaDate;

    private String bull;

    @ManyToOne
    private Animal animal;

}
