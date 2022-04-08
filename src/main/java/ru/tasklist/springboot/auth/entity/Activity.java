package ru.tasklist.springboot.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class Activity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "activated")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean activated;
    @NotBlank
    @Column(name = "uuid", updatable = false)
    private String uuid;
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

/*    @Column(name = "user_id", nullable = false)
    private Long userId;*/
}
