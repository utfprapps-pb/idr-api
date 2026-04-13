package br.edu.utfpr.ProjetoIDRAPI.entity.reproductiveCycle;

import br.edu.utfpr.ProjetoIDRAPI.cowFormulation.core.domain.enums.ProductionStage;
import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReproductiveCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Animal animal;

    //    Ex: 1, 2, 3...
    private Integer lactationNumber;

    //    Data do parto que iniciou este ciclo
    private LocalDate calvingDate;

    //    Pode ser atualizado ao longo do ciclo
    private LocalDate lastInseminationDate;

    //    Lactação, Seca, Pré-parto - Status atual deste ciclo
    @Enumerated(EnumType.STRING)
    private ProductionStage stage;
}
