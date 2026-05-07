package br.gov.pr.idr.legacy.entity.city;

import br.gov.pr.idr.legacy.entity.region.Region;
import br.gov.pr.idr.legacy.enums.State;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;
}
