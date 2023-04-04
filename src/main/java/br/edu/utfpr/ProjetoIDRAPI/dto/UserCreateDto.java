package br.edu.utfpr.ProjetoIDRAPI.dto;

import javax.validation.constraints.NotNull;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import lombok.Data;

@Data
public class UserCreateDto {
	private long id;
	
	@NotNull
    private String username;
	
	@NotNull
    private String displayName;
    
	@NotNull
    private String password;
    
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
		user.setPassword(password);
		user.setProfessionalRegister(professionalRegister);
    	
    	return user;
    }

}
