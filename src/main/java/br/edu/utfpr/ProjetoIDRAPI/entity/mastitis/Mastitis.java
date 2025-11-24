package br.edu.utfpr.ProjetoIDRAPI.entity.mastitis;

import br.edu.utfpr.ProjetoIDRAPI.entity.animal.Animal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mastitis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate diagnoseDate;

    private String mastitisType;

    private String cmtResult;

    private String ad;

    private String ae;

    private String pd;

    private String pe;

    @ManyToOne
    private Animal animal;

}
