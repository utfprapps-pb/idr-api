package br.gov.pr.idr.legacy.entity.user;

import br.gov.pr.idr.legacy.entity.compositeuserregion.CompositeUserRegion;
import br.gov.pr.idr.legacy.entity.region.Region;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion {

    @EmbeddedId
    private CompositeUserRegion id;

    @NotNull
    @MapsId("user")
    @ManyToOne
    private User user;

    @NotNull
    @MapsId("region")
    @ManyToOne
    private Region region;
}
