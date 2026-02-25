package br.edu.utfpr.ProjetoIDRAPI.entity.formulation;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Animal animal;

    private LocalDate balanceDate;

    private LocalDate nextVisitDate;

    private BigDecimal ambientTemperature;

    private BigDecimal weightChangeGoal;

}
