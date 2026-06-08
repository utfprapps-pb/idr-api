package br.gov.pr.idr.infra.property_management.city.persistence;

import br.gov.pr.idr.domain.property_management.city.vo.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.UUID;

@Entity(name = "City")
@Table(name = "city")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
public class CityJPAEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = false)
    private State state;

    @Column(name = "region_id", nullable = false)
    private UUID regionId;
}
