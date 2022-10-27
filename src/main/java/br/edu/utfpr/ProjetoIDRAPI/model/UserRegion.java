package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
