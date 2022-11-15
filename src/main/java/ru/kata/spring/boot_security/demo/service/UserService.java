package com.example.pp_3_1_2.service;


import com.example.pp_3_1_2.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void createUser(User user);

    User get(int id);
//
    void update(int id, User updatedUser);
//
    void delete(int id);
}
