package br.gov.pr.idr.infra.property_management.region.persistence;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity(name = "Region")
@Table(name = "region")
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Getter
@Setter
public class RegionJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

}