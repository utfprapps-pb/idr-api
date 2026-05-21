package br.gov.pr.idr.legacy.entity.user;

import br.gov.pr.idr.legacy.entity.user.annotation.ValidUser;
import br.gov.pr.idr.legacy.utils.BaseUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidUser
public class User implements UserDetails, BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @Transient
    private String confirmPassword;

    @Column(unique = true)
    private String cpf;

    private String phone;

    private String cep;

    private String street;

    private String houseNumber;

    private String professionalRegister;

    private String graduationYear;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.addAll(this.userPermissions);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}