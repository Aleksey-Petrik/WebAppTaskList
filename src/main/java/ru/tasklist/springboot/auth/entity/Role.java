package ru.tasklist.springboot.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "ROLE_DATA", schema = "tasklist", catalog = "todolist")
public class Role {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;

/*    @OneToMany(mappedBy = "roleDataByRoleId")
    private Collection<UserRole> userRolesById;*/
}
