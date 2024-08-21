package br.edu.utfpr.ProjetoIDRAPI.entity.user;

import br.edu.utfpr.ProjetoIDRAPI.entity.crud.CrudService;
import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;

public interface UserService extends CrudService<User, Long> {

	User findByName(String username);

	User findSelfUser();

}
