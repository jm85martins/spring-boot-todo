package com.jm85martins.todo.entities;

import lombok.*;

/**
 * Created by jorgemartins on 17/07/2017.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Item {

    private String name;

    private ItemState state = ItemState.TODO;

    public enum ItemState {TODO, DONE}
}
