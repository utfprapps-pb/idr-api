package br.edu.utfpr.ProjetoIDRAPI.entity.user.validation;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.utils.BaseUser;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserValidation {

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValid(BaseUser entity) {
        return validateUniqueCPF(entity) && validateUniqueUsername(entity);
    }

    private boolean validateUniqueCPF(BaseUser entity) {
        User user = userRepository.findByCpfAndCpfIsNotNull(entity.getCpf());
        boolean valid = user == null || Objects.equals(user.getId(), entity.getId());

        if (!valid) {
            throw new IllegalArgumentException("O CPF informado já está em uso");
        }

        return true;
    }

    private boolean validateUniqueUsername(BaseUser entity) {
        User user = userRepository.findByUsername(entity.getUsername());
        boolean valid = user == null || Objects.equals(user.getId(), entity.getId());

        if (!valid) {
            throw new IllegalArgumentException("O nome de usuário informado já está em uso");
        }

        return true;
    }
}
