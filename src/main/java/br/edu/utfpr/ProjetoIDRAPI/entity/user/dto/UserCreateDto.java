package br.edu.utfpr.ProjetoIDRAPI.entity.user.dto;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.annotation.ValidUser;
import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ValidUser
public class UserCreateDto implements BaseUser {
	private long id;
	
	@NotNull
    private String username;
	
	@NotNull
    private String displayName;
    
	@NotNull
    private String password;

    private String cpf;
    
    private String city;

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
