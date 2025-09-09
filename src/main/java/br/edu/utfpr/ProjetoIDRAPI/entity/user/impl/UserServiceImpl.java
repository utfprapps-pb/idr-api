package br.edu.utfpr.ProjetoIDRAPI.entity.user.impl;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.impl.CrudServiceImpl;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
	
	@Override
	public User findByName(String username) {
		return userRepository.findByUsername(username);
	}

    @Override
    public User findSelfUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.toString());
        return user;
    }

}
