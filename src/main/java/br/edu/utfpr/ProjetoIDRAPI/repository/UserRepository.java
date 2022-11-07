package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByusername(String username);

}
