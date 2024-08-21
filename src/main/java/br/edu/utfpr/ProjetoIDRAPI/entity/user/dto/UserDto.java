package br.edu.utfpr.ProjetoIDRAPI.entity.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private long id;

    private String username;

    private String displayName;
    
    private String cpf;
    
    private String county;

    private String cep;

    private String street;

    private String houseNumber;

    private String phone;

    private String professionalRegister;

    private String graduationYear;
}
