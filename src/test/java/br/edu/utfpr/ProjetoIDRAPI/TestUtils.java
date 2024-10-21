package br.edu.utfpr.ProjetoIDRAPI;

import br.edu.utfpr.ProjetoIDRAPI.entity.user.User;
import br.edu.utfpr.ProjetoIDRAPI.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Set;

public class TestUtils {

    public static User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserPermissions(Set.of());
        return user;
    }
}
