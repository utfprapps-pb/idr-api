package br.gov.pr.idr.legacy.entity.token.dto;

import br.gov.pr.idr.legacy.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenDto {
    private String username;

    private String name;
    
    private Set<AuthorityDto> authorities;
    
    public UserTokenDto(User user) {
    	this.name = user.getName();
    	this.username = user.getUsername();
    	this.authorities = new HashSet<>();
    	
    	for(GrantedAuthority authority: user.getAuthorities()) {
    		authorities.add(new AuthorityDto(authority.getAuthority()));
    	}
    }
}
