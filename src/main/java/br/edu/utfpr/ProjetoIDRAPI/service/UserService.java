package br.edu.utfpr.ProjetoIDRAPI.service;

import java.util.List;

import br.edu.utfpr.ProjetoIDRAPI.model.User;

public interface UserService {

    User save(User user);

    User findOne(Long id);

    List<User> findAll();

    void delete(Long id);
}
