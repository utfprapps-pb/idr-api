package br.edu.utfpr.ProjetoIDRAPI.entity.user.dto;

import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import lombok.Data;

@Data
public class UserDto implements BaseUser {
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
