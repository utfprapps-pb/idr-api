package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CompositeUserRegion implements Serializable {
    private long user;

    private long region;
}
