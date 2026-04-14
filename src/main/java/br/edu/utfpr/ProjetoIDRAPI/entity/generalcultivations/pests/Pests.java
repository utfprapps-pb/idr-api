package br.edu.utfpr.ProjetoIDRAPI.entity.generalcultivations.pests;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pests")
public class Pests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
}
