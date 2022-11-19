package ru.kata.spring.boot_security.demo.service;




import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void createUser(User user);

    User get(int id);
//
    void update(int id, User updatedUser);
//
    void delete(int id);

    public User findByUsername(String username);


}
