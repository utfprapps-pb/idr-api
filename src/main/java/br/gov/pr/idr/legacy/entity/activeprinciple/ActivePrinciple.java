package br.gov.pr.idr.legacy.entity.activeprinciple;

import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Mantida apenas para Product (legacy) compilar — ver Plano D de migração; sem controller/service próprio.
@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivePrinciple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

}
