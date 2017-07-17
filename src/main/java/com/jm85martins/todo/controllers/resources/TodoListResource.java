package com.jm85martins.todo.controllers.resources;

import com.jm85martins.todo.controllers.TodoListController;
import com.jm85martins.todo.entities.TodoList;
import org.springframework.hateoas.ResourceSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by jorgemartins on 03/07/2017.
*/
public class TodoListResource extends ResourceSupport {

    private final TodoList todoList;

    public TodoListResource(TodoList todoList){
        this.todoList = todoList;
        this.add(linkTo(methodOn(TodoListController.class, todoList.getId())
                .getTodoList(todoList.getUserId(), todoList.getId())).withSelfRel());
    }

    public TodoList getContent(){
        return this.todoList;
    }
}
