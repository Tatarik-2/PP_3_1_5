package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.RoleDAO;
import ru.kata.spring.boot_security.demo.DAO.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDAO userDAO;
    private UserRepository userRepository;
    private RoleDAO roleDAO;

    public UserServiceImpl(UserDAO userDAO, UserRepository userRepository, RoleDAO roleDAO
    ) {
        this.userDAO = userDAO;
        this.userRepository = userRepository;
        this.roleDAO = roleDAO;
    }

//    //    @Autowired
//    public UserServiceImpl(UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }
//
//    @Autowired//пока решил так оставить, через аутоуайрд
//    public void setUserRepository(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователя с именем %s не существует", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword()
                , mapRolesToAuthorities(user.getRoles()));
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isEmpty()){
//            throw new UsernameNotFoundException(String.format("Пользователя с именем %s не существует", username));
//        }
//        return user.get();
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    //    public Optional<User> findByUsername(String username){
//        return userRepository.findByUsername(username);
//    }


    @Override
    public List<Role> listRoles() {
        return roleDAO.listRoles();
    }

    @Override
    public List<Role> listByRole(List<String> name) {
        return roleDAO.listByName(name);
    }
}
