package ru.tasklist.springboot.business.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tasklist.springboot.auth.entity.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "completed_count", updatable = false)
    private Long completedCount;
    @Column(name = "uncompleted_count", updatable = false)
    private Long uncompletedCount;
/*    @Column(name = "user_id")
    private Long userId;*/

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
