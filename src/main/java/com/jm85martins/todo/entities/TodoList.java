package com.jm85martins.todo.entities;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by jorgemartins on 30/06/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class TodoList {
    @Id
    private String id;

    private String name;

    private String userId;

    private List<Item> items;
}
