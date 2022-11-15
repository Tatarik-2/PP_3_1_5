package com.example.pp_3_1_2.DAO;


import com.example.pp_3_1_2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

//    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
//    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(int id, User user) {
        User userForUpdate = get(id);
        userForUpdate.setName(user.getName());
        userForUpdate.setEmail(user.getEmail());
        userForUpdate.setAge(user.getAge());
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
