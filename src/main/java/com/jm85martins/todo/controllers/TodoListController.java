package com.jm85martins.todo.controllers;

import com.jm85martins.todo.controllers.resources.TodoListResource;
import com.jm85martins.todo.entities.TodoList;
import com.jm85martins.todo.repositories.TodoListRepository;
import com.jm85martins.todo.utils.ErrorObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jorgemartins on 30/06/2017.
 */
@Controller
@RequestMapping("/{userId}/todo-list")
public class TodoListController {
    public static final Logger logger = LoggerFactory.getLogger(TodoListController.class);

    private TodoListRepository todoListRepository;

    @Autowired
    TodoListController(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Resources<TodoListResource>> getMyTodoLists(@PathVariable String userId) {
        logger.info("Getting todo list for user {}", userId);

        List<TodoListResource> todoListResources = this.todoListRepository
                .findByUserId(userId).stream().map(TodoListResource::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new Resources<>(todoListResources));
    }

    @RequestMapping(path = "/{listId}", method = RequestMethod.GET)
    public ResponseEntity<TodoListResource> getTodoList(@PathVariable String userId, @PathVariable String listId) {
        logger.info("Getting todo for user {} and todo list {}", userId, listId);

        Optional<TodoList> todoList = this.todoListRepository.findById(listId);

        if (!todoList.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TodoListResource(todoList.get()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TodoListResource> newTodoList(@PathVariable String userId, @RequestBody TodoList todoList) {
        logger.info("Creating todo for user {} with name {}", userId, todoList.getName());

        todoList.setUserId(userId);
        todoList = this.todoListRepository.insert(todoList);

        TodoListResource todoResource = new TodoListResource(todoList);

        return ResponseEntity.ok(todoResource);
    }

    @RequestMapping(path = "/{listId}", method = RequestMethod.PUT)
    public ResponseEntity<TodoListResource> updateTodoList(@PathVariable String userId, @PathVariable String listId,
                                                           @RequestBody TodoList todoList) {
        logger.info("Updating todo for user {} with id {}", userId, listId);

        Optional<TodoList> managedList = this.todoListRepository.findById(listId);

        if (managedList.isPresent()) {
            todoList.setUserId(userId);
            todoList = this.todoListRepository.save(todoList);
            return ResponseEntity.ok(new TodoListResource(todoList));
        } else {
            logger.error("Unable to update the Todo List. Todo List with id {} not found.", listId);
            return new ResponseEntity(
                    ErrorObj.builder()
                            .userMessage("Unable to update the Todo List. Todo List with id ".concat(listId).concat("not found."))
                            .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

}
