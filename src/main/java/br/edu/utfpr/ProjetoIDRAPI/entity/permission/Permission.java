package br.edu.utfpr.ProjetoIDRAPI.entity.permission;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;


@Entity @Audited
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
