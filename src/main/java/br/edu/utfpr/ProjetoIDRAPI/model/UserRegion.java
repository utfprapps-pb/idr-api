package br.edu.utfpr.ProjetoIDRAPI.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion {

    @EmbeddedId
    private CompositeUserRegion id;

    @MapsId("user")
    @ManyToOne
    private User user;

    @MapsId("region")
    @ManyToOne
    private Region region;
}
