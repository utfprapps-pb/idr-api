package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "Property_id")
    @ManyToOne
    private Property property;

    private LocalDate useDate;

    private Integer quantity;

    @NotNull
    @NotEmpty
    private String usedFor;
}
