package br.edu.utfpr.ProjetoIDRAPI.entity.milkControl;

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
public class MilkControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Animal animal;

    //    Data da medição
    private LocalDate controlDate;

    //    Litros/Kg no dia
    private BigDecimal milkYield;

    private BigDecimal fatPercentage;

    private BigDecimal proteinPercentage;

}
