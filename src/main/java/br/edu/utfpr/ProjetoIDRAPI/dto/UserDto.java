package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import lombok.Data;

@Data
public class UserDto {

    private long id;

    private String username;
    
    private String password;
    
    private String cpf;
    
    private String county;

    private String cep;

    private String street;

    private Integer houseNumber;

    private String phone;

    private String professionalRegister;

    private String graduationYear;

    private String email;
    
    public User toUser() {
    	User user = new User();
    	user.setId(id);  
		user.setUsername(username);
		user.setPhone(phone);
		user.setProfessionalRegister(professionalRegister);
    	
    	return user;
    }
}
