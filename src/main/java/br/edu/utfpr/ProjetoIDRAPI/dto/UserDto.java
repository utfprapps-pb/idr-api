package br.edu.utfpr.ProjetoIDRAPI.dto;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
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
    
    public User toUser() {
    	User user = new User();
    	user.setId(id);  
		user.setUsername(username);
		user.setDisplayName(displayName);
		user.setProfessionalRegister(professionalRegister);
    	
    	return user;
    }
}
