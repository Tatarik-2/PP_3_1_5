package ru.kata.spring.boot_security.demo.DAO;



import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


//@Repository
//@Scope(proxyMode = ScopedProxyMode.INTERFACES)
@Component
//@Transactional
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public UserDAOImpl(EntityManager entityManager, @Lazy PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        entityManager.persist(user);
    }

    @Override
    public void update(int id, User user) {
        User userForUpdate = get(id);
        userForUpdate.setUsername(user.getUsername());
        userForUpdate.setName(user.getName());
        userForUpdate.setEmail(user.getEmail());
        userForUpdate.setAge(user.getAge());
        userForUpdate.setRoles(user.getRoles());
        if (user.getPassword().length()>0){
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            userForUpdate.setPassword(encodedPassword);
        }
    }

    @Override
    public User get(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void removeUserById(int id) {
        entityManager.remove(get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

//    @Override
//    public void cleanUsersTable() {
//
//    }
}
