package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserSpecExecutor {

    User findByUsername(String username);

    User findByCpfAndCpfIsNotNull(String cpf);
}
