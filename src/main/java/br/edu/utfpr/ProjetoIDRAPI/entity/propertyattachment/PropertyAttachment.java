package br.edu.utfpr.ProjetoIDRAPI.entity.propertyattachment;

import br.edu.utfpr.ProjetoIDRAPI.entity.property.Property;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity @Audited
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "bytea")
    private byte[] attachment;

    public PropertyAttachment(Property property, byte[] attachment) {
        this.property = property;
        this.attachment = attachment;
    }
}
