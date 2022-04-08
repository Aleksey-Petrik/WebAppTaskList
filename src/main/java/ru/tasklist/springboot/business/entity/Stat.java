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
@NoArgsConstructor
@EqualsAndHashCode
public class Stat {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "completed_total", updatable = false)
    private Long completedTotal;
    @Column(name = "uncompleted_total", updatable = false)
    private Long uncompletedTotal;
/*    @Column(name = "user_id")
    private Long userId;*/

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
