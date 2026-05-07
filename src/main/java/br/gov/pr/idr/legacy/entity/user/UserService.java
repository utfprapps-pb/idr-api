package br.gov.pr.idr.legacy.entity.user;

import br.gov.pr.idr.legacy.entity.crud.CrudService;

public interface UserService extends CrudService<User, Long> {

	User findByName(String username);

	User findSelfUser();

}
