package ru.tasklist.springboot.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "USER_DATA", schema = "tasklist", catalog = "todolist")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Activity activity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    /*    @OneToMany(mappedBy = "user")
    private Collection<Activity> activities;

    @OneToMany(mappedBy = "user")
    private Collection<Category> categoriesById;

    @OneToMany(mappedBy = "user")
    private Collection<Priority> prioritiesById;

    @OneToMany(mappedBy = "user")
    private Collection<Stat> statsById;

    @OneToMany(mappedBy = "user")
    private Collection<Task> tasksById;*/

    /*    @OneToOne(mappedBy = "userDataByUserId")
    private UserRole userRoleById;*/
}
