package br.gov.pr.idr.legacy.entity.property;

import br.gov.pr.idr.legacy.entity.city.City;
import br.gov.pr.idr.legacy.entity.propertyarea.PropertyArea;
import br.gov.pr.idr.legacy.entity.propertycollaborator.PropertyCollaborator;
import br.gov.pr.idr.legacy.entity.propertytechnician.PropertyTechnician;
import br.gov.pr.idr.legacy.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity @Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private User producer;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private BigDecimal totalArea;

    private BigInteger latitude;

    private BigInteger longitude;

    private Boolean leased;

    private Double nakedAveragePrice;

    private Double leaseAveragePrice;

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private PropertyArea area;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyCollaborator> collaborators;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyTechnician> technicians;

}