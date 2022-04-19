package ru.tasklist.springboot.business.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchValues {
    //Поля для поиска, все ссылочные для того чтобы была возможность передать null
    private String title;
    private Integer completed;
    private Long priorityId;
    private Long categoryId;
    private String email;
    private Date dateFrom;
    private Date dateTo;
    //Постраничность
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    //Сортировка
    private String sortColumn;
    private String sortDirection;
}
