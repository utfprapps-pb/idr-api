package br.edu.utfpr.ProjetoIDRAPI.entity.pregnancydiagnose;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PregnancyDiagnose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate diagnoseDate;

    private LocalDate lastIA;

    @ManyToOne
    private Animal animal;

}
