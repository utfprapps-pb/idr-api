package br.edu.utfpr.ProjetoIDRAPI.service.impl;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import br.edu.utfpr.ProjetoIDRAPI.repository.UserRepository;
import br.edu.utfpr.ProjetoIDRAPI.service.UserService;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        user.setCpf(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

	@Override
	public User findOne(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
}
