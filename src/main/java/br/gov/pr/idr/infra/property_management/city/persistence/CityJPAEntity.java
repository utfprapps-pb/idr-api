package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.State;
import br.gov.pr.idr.infra.property_management.region.persistence.RegionJPAEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = "City")
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
public class CityJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @ManyToOne
    private RegionJPAEntity region;

    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;
}
