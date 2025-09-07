package br.edu.utfpr.ProjetoIDRAPI.entity.propertymap;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "soil_map")
    private Byte[] soilMap;
}