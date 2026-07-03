package br.gov.pr.idr.domain.iam.user;

import br.gov.pr.idr.domain.iam.permission.Permission;
import br.gov.pr.idr.domain.iam.user.vo.CPF;
import br.gov.pr.idr.domain.iam.user.vo.Password;
import br.gov.pr.idr.domain.property_management.city.CityID;
import br.gov.pr.idr.domain.shared.tactical.AggregateRoot;
import br.gov.pr.idr.domain.shared.tactical.validation.DomainError;
import br.gov.pr.idr.domain.shared.tactical.validation.ValidationHandler;

import java.time.Instant;
import java.util.Set;

public class User extends AggregateRoot<UserID> {
    private final String name;
    private final String username;
    private final Instant createdAt;
    private final CPF cpf;
    private Password password;
    private Instant updatedAt;
    private boolean active;
    private String phone;
    private CityID cityID;
    private String cep;
    private String street;
    private String houseNumber;
    private String professionalRegister;
    private String graduationYear;
    private Set<Permission> permissions;

    User(final UserID id,
         final String name,
         final String username,
         final Password password,
         final CPF cpf,
         final String phone,
         final CityID cityID,
         final String cep,
         final String street,
         final String houseNumber,
         final String professionalRegister,
         final String graduationYear,
         final Instant createdAt,
         final Instant updatedAt,
         final boolean active,
         Set<Permission> permissions
    ) {
        super(id);
        this.name = name;
        this.username = username;
        this.password = password;
        this.cpf = cpf;
        this.phone = phone;
        this.cityID = cityID;
        this.cep = cep;
        this.street = street;
        this.houseNumber = houseNumber;
        this.professionalRegister = professionalRegister;
        this.graduationYear = graduationYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.permissions = permissions;
    }

    public static User create(final String name,
                              final String username,
                              final Password password,
                              final CPF cpf,
                              final String phone,
                              final CityID city,
                              final String cep,
                              final String street,
                              final String houseNumber,
                              final String professionalRegister,
                              final String graduationYear,
                              final Set<Permission> permissions
    ) {
        final var user = new User(UserID.unique(), name, username, password, cpf, phone, city, cep, street, houseNumber,
                professionalRegister, graduationYear, Instant.now(), Instant.now(), false, permissions);
        user.selfValidate();
        return user;
    }

    public static User with(final UserID id,
                            final String name,
                            final String username,
                            final Password password,
                            final CPF cpf,
                            final String phone,
                            final CityID city,
                            final String cep,
                            final String street,
                            final String houseNumber,
                            final String professionalRegister,
                            final String graduationYear,
                            final Instant createdAt,
                            final Instant updatedAt,
                            final boolean active,
                            final Set<Permission> permissions
    ) {
        return new User(id, name, username, password, cpf, phone, city, cep, street, houseNumber, professionalRegister,
                graduationYear, createdAt, updatedAt, active, permissions);
    }

    public User update(final String phone,
                       final CityID city,
                       final String cep,
                       final String street,
                       final String houseNumber,
                       final String professionalRegister,
                       final String graduationYear,
                       final Password password,
                       final boolean active,
                       final Set<Permission> permissions
    ) {
        this.phone = phone;
        this.cityID = city;
        this.cep = cep;
        this.street = street;
        this.houseNumber = houseNumber;
        this.professionalRegister = professionalRegister;
        this.graduationYear = graduationYear;
        this.password = password;
        this.active = active;
        this.updatedAt = Instant.now();
        this.permissions = permissions;
        super.selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler handler) {
        if (this.name == null || this.name.isBlank()) {
            handler.append(DomainError.from("name", "Nome do usuário não pode ser nulo"));
        }
        if (this.username == null || this.username.isBlank()) {
            handler.append(DomainError.from("username", "UserName não pode ser nulo"));
        }
        if (this.cpf == null) {
            handler.append(DomainError.from("cpf", "CPF não pode ser nulo"));
        }
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public CPF getCpf() {
        return cpf;
    }

    public Password getPassword() {
        return password;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public String getPhone() {
        return phone;
    }

    public CityID getCityID() {
        return cityID;
    }

    public String getCep() {
        return cep;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getProfessionalRegister() {
        return professionalRegister;
    }

    public String getGraduationYear() {
        return graduationYear;
    }

    public Set<Permission> getPermissions() {return permissions;}
}
