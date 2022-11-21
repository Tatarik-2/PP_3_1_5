package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
@Component
public class RoleDAOImpl implements RoleDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> listRoles() {
        return entityManager.createQuery("select s from Role s", Role.class).getResultList();
    }

    @Override
    public List<Role> listByName(List<String> name) {
        return  entityManager.createQuery("select u FROM Role u WHERe u.role in (:id)", Role.class)
                .setParameter("id", name)
                .getResultList();
    }
}
