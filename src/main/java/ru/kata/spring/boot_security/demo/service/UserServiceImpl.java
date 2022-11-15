package com.example.pp_3_1_2.service;

import com.example.pp_3_1_2.DAO.UserDAO;
import com.example.pp_3_1_2.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserDAO userDAO;
//    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void createUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User get(int id) {
        return userDAO.get(id);
    }

    @Override
    @Transactional
    public void update(int id, User updatedUser) {
        userDAO.update(id, updatedUser);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDAO.removeUserById(id);
    }

}
