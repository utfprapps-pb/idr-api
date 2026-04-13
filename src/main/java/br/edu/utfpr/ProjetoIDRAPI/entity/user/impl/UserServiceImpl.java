package br.edu.utfpr.ProjetoIDRAPI.entity.user.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.validation.UserValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserValidation userValidation) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
        this.userValidation = userValidation;
    }

    @Override
    protected JpaRepository<User, Long> getRepository() {
        return this.userRepository;
    }

    @Override
    public JpaSpecificationExecutor<User> getSpecExecutor() {
        return this.userRepository;
    }

    @Override
    public User save(User user) {
        userValidation.isValid(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findSelfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();
        return userRepository.findByUsername(principal.toString());
    }

}
