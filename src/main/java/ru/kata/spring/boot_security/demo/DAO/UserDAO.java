package com.example.pp_3_1_2.DAO;

import com.example.pp_3_1_2.model.User;

import java.util.List;

public interface UserDAO {
//    void createUsersTable();

//    void dropUsersTable();

    void saveUser(User user);

    void update(int id, User user);

    User get(int id);

    void removeUserById(int id);

    List<User> getAllUsers();

//    void cleanUsersTable();
}
