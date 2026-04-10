package br.edu.utfpr.ProjetoIDRAPI.entity.user.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.city.City;
import br.edu.utfpr.ProjetoIDRAPI.entity.city.dto.CityDto;
import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class UserDto implements BaseUser {

    private Long id;

    private String name;

    private String username;

    private String password;

    private String confirmPassword;

    private String cpf;

    private String phone;

    private String graduationYear;

    private String professionalRegister;

    private String cep;

    private String street;

    private CityDto city;

    private String houseNumber;

}
