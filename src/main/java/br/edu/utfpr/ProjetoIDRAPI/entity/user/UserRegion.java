package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import br.edu.utfpr.ProjetoIDRAPI.entity.compositeuserregion.CompositeUserRegion;
import br.edu.utfpr.ProjetoIDRAPI.entity.region.Region;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
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
