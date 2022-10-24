package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String username;

    private Integer number;

    private String phone;

    private String professionalRegister;

    private String graduationYear;

    private String email;
}
