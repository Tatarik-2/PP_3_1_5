package ru.kata.spring.boot_security.demo.DAO;


import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
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
        if (user.getPassword().length() > 0) {
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

    @Override
    public User findUserByUsername(String username) throws UsernameNotFoundException {
//        return entityManager.createQuery("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class)
//                .setParameter("username", username)
//                .getSingleResult();

//        Query query = entityManager.createQuery("SELECT u FROM User u where u.username =:username", User.class);
//        query.setParameter("username", username);
//        Optional<User> user = Optional.ofNullable((User)query.getResultList().get(0));
//
////        Optional<User> user = Optional.ofNullable(entityManager.createQuery("SELECT u FROM User u where u.username =:username", User.class).getResultList().get(0));
//        if (user.isEmpty()) {
//            throw new UsernameNotFoundException("User not found!");
//        }
//        return user.get();

        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User AS u JOIN FETCH u.roles WHERE u.username= :username", User.class);

        query.setParameter("username", username);

        if (username == null) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = query.getSingleResult();
        System.out.println("55555555555" + user.getRoles());
        return user;
    }

    //    @Override
//    public void cleanUsersTable() {
//
//    }
}
