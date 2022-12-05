package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    void createUser(User user);

    User get(int id);

    //
    void update(User updatedUser);

    //
    void delete(int id);

    User getUserByUsername(String username);

    List<Role> listRoles();

}
