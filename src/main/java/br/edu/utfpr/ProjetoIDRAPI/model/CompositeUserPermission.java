package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CompositeUserPermission implements Serializable {
    private Long user;

    private Long permission;
}
