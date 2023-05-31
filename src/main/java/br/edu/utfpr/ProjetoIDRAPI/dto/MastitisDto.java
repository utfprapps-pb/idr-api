package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.Animal;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MastitisDto {

    private long id;

    private LocalDate diagnoseDate;

    private String mastitisType;

    private String cmtResult;

    private String ad;

    private String ae;

    private String pd;

    private String pe;

    private Animal animal;

}
