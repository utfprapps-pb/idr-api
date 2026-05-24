package br.gov.pr.idr.infra.iam.user.persistence;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.User;
import br.gov.pr.idr.domain.iam.user.UserID;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.infra.iam.permission.persistence.PermissionJPAEntity;
import br.gov.pr.idr.infra.property_management.city.persistence.CityJPAEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity(name = "User")
@Table(name = "users")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJPAEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    @Column(unique = true)
    private String cpf;

    private String phone;
    private String cep;
    private String street;
    private String houseNumber;
    private String professionalRegister;
    private String graduationYear;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityJPAEntity city;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_permission",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    private Set<PermissionJPAEntity> userPermissions;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(this.userPermissions);
    }

    @Override
    public String getPassword() {
        return password;
    }

    public static UserJPAEntity from(final User user, final CityJPAEntity city, final String encodePassword) {
        return new UserJPAEntity(
                user.getId().id(),
                user.getName(),
                user.getUsername(),
                encodePassword,
                user.getCpf().value(),
                user.getPhone(),
                user.getCep(),
                user.getStreet(),
                user.getHouseNumber(),
                user.getProfessionalRegister(),
                user.getGraduationYear(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive(),
                city,
                fromDomainPermissions(user.getPermissions()));
    }

    public User toDomain() {
        return User.with(
                UserID.from(this.id),
                this.name,
                this.username,
                Password.from(this.password, this.password),
                CPF.from(this.cpf),
                this.phone,
                CityID.from(this.getCityId()),
                this.cep,
                this.street,
                this.houseNumber,
                this.professionalRegister,
                this.graduationYear,
                this.createdAt,
                this.updatedAt,
                this.active,
                this.getDomainPermissions()
        );
    }

    private Long getCityId() {
        return Optional.ofNullable(this.city)
                .map(CityJPAEntity::getId)
                .orElse(null);
    }

    private Set<Permission> getDomainPermissions() {
        return this.userPermissions.stream().map(PermissionJPAEntity::toDomain)
                .collect(Collectors.toSet());
    }

    private static Set<PermissionJPAEntity> fromDomainPermissions(final Set<Permission> permissions) {
        return permissions.stream().map(PermissionJPAEntity::from)
                .collect(Collectors.toSet());
    }

}