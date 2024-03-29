package br.edu.utfpr.ProjetoIDRAPI.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "User_id")
    @ManyToOne
    private User user;

    private String ocupationArea;

    private BigDecimal totalArea;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "soil_map")
    private Byte[] soilMap;

    private BigInteger latitude;

    private BigInteger longitude;

    @NotNull
    private Boolean leased;
}
