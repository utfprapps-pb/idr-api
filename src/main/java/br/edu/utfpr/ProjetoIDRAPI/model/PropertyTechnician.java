package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyTechnician implements Serializable {

    @Id
    @JoinColumn(name = "User_id")
    @ManyToOne
    private User user;

    @Id
    @JoinColumn(name = "Property_id")
    @ManyToOne
    private Property property;
}
