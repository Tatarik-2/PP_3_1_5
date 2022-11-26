package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import ru.kata.spring.boot_security.demo.DAO.RoleDAO;
//import ru.kata.spring.boot_security.demo.DAO.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private EntityManager entityManager;
    private UserRepository userRepository;
    private RoleRepository rolesRepository;



    public UserServiceImpl(
            UserRepository userRepository,
            EntityManager entityManager,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.rolesRepository = roleRepository;
    }

    @Override
    @Transactional
    public void createUser(User user) {
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
//        userRepository.save(user);
        entityManager.persist(user);

    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void update(int id, User updatedUser) {
        User userForUpdate = get(id);
        userForUpdate.setUsername(updatedUser.getUsername());
        userForUpdate.setName(updatedUser.getName());
        userForUpdate.setEmail(updatedUser.getEmail());
        userForUpdate.setAge(updatedUser.getAge());
        userForUpdate.setRoles(updatedUser.getRoles());
        if (updatedUser.getPassword().length() > 0) {
            String encodedPassword = new BCryptPasswordEncoder().encode(updatedUser.getPassword());
            userForUpdate.setPassword(encodedPassword);
        }
        userRepository.save(userForUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public User get(int id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with %s not found", id));
        }
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователя с именем %s не существует", username));
        }
        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }

    @Override
    public List<Role> listRoles() {
        return rolesRepository.findAll();
    }

}
