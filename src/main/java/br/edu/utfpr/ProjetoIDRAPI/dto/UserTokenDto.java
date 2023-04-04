package br.edu.utfpr.ProjetoIDRAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import br.edu.utfpr.ProjetoIDRAPI.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenDto {
    private String username;

    private String displayName;
    
    private Set<AuthorityDto> authorities;
    
    public UserTokenDto(User user) {
    	this.displayName = user.getDisplayName();
    	this.username = user.getUsername();
    	this.authorities = new HashSet<>();
    	
    	for(GrantedAuthority authority: user.getAuthorities()) {
    		authorities.add(new AuthorityDto(authority.getAuthority()));
    	}
    }
}
