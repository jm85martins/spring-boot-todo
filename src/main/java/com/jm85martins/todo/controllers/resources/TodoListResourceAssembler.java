package com.jm85martins.todo.controllers.resources;

import com.jm85martins.todo.controllers.TodoListController;
import com.jm85martins.todo.entities.TodoList;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by jorgemartins on 19/07/2017.
 */
@Component
public class TodoListResourceAssembler extends ResourceAssemblerSupport<TodoList, Resource> {

    public TodoListResourceAssembler() {
        super(TodoListController.class, Resource.class);
    }

    @Override
    public Resource toResource(TodoList todoList) {
        return new Resource<TodoList>(todoList, linkTo(methodOn(TodoListController.class, todoList.getId())
                .getTodoList(todoList.getUserId(), todoList.getId())).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends TodoList> todoList) {
        List<Resource> resources = new ArrayList<Resource>();
        for(TodoList todoL : todoList) {
            resources.add(new Resource<TodoList>(todoL, linkTo(methodOn(TodoListController.class, todoL.getId())
                    .getTodoList(todoL.getUserId(), todoL.getId())).withSelfRel()));
        }
        return resources;
    }
}
