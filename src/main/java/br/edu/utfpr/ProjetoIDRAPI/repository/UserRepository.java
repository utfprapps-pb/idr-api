package br.edu.utfpr.ProjetoIDRAPI.repository;

import br.edu.utfpr.ProjetoIDRAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	//método que fará a busca no banco pelo nome do usuário que foi 
	//passado por parâmetro e retornará um usuário
    User findByUsername(String username);
}
