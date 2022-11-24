package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Column
    @Id
    private Integer id;
    @Column
    private String role;

    //    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "user_roles"
//            , joinColumns = @JoinColumn(name = "role_id")
//            , inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToUser(User user) {
        this.users.add(user);
    }

    @Override
    public String getAuthority() {
        return getRole();
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    public Role() {
    }

    @Override
    public String toString() {
        return role.replace("ROLE_", "");
    }
}
