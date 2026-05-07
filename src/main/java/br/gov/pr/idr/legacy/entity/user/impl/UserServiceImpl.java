package br.gov.pr.idr.legacy.entity.user.impl;

import br.gov.pr.idr.legacy.entity.city.City;
import br.gov.pr.idr.legacy.entity.city.CityService;
import br.gov.pr.idr.legacy.entity.crud.impl.CrudServiceImpl;

import br.gov.pr.idr.legacy.entity.user.User;
import br.gov.pr.idr.legacy.entity.user.UserRepository;
import br.gov.pr.idr.legacy.entity.user.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CrudServiceImpl<User, Long> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityService cityService;

    public UserServiceImpl(UserRepository userRepository, CityService cityService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cityService = cityService;
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
        if (user.getCity().getId() == null || user.getCity().getId() == 0) {
            City city = this.cityService.findByName(user.getCity().getName());
            if (city != null) {
                user.setCity(city);
            }
        }
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
