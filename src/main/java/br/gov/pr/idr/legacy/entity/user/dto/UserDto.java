package br.gov.pr.idr.legacy.entity.user.dto;

import br.gov.pr.idr.legacy.entity.city.dto.CityDto;
import br.gov.pr.idr.legacy.utils.BaseUser;
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
