package com.jm85martins.todo.controllers;

import com.jm85martins.todo.controllers.resources.TodoListResourceAssembler;
import com.jm85martins.todo.entities.TodoList;
import com.jm85martins.todo.exceptions.TodoListNotFoundException;
import com.jm85martins.todo.repositories.TodoListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

/**
 * Created by jorgemartins on 30/06/2017.
 */
@Controller
@RequestMapping("/{userId}/todo-list")
public class TodoListController {
    public static final Logger logger = LoggerFactory.getLogger(TodoListController.class);

    private TodoListRepository todoListRepository;

    private TodoListResourceAssembler todoListResourceAssembler;

    @Autowired
    TodoListController(TodoListRepository todoListRepository, TodoListResourceAssembler todoListResourceAssembler) {
        this.todoListRepository = todoListRepository;
        this.todoListResourceAssembler = todoListResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PagedResources<TodoList>> getMyTodoLists(@PathVariable String userId,
                                                                   Pageable pageable, PagedResourcesAssembler assembler) {
        logger.info("Getting todo list for user {}", userId);

        Page<TodoList> listPage = this.todoListRepository.findByUserId(userId, pageable);

        return ResponseEntity.ok(assembler.toResource(listPage, todoListResourceAssembler));
    }

    @RequestMapping(path = "/{listId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getTodoList(@PathVariable String userId, @PathVariable String listId) {
        logger.info("Getting todo for user {} and todo list {}", userId, listId);

        Optional<TodoList> todoList = this.todoListRepository.findByIdAndUserId(listId, userId);

        if (!todoList.isPresent()) {
            throw new TodoListNotFoundException(listId, userId);
        }

        return ResponseEntity.ok(todoListResourceAssembler.toResource(todoList.get()));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Resource> newTodoList(@PathVariable String userId, @RequestBody TodoList todoList) {
        logger.info("Creating todo for user {} with name {}", userId, todoList.getName());

        todoList.setUserId(userId);
        todoList = this.todoListRepository.insert(todoList);

        return ResponseEntity.ok(todoListResourceAssembler.toResource(todoList));
    }

    @RequestMapping(path = "/{listId}", method = RequestMethod.PUT)
    public ResponseEntity<Resource> updateTodoList(@PathVariable String userId, @PathVariable String listId,
                                                   @RequestBody TodoList todoList) {
        logger.info("Updating todo for user {} with id {}", userId, listId);

        Optional<TodoList> managedList = this.todoListRepository.findByIdAndUserId(listId, userId);

        if (managedList.isPresent()) {
            TodoList toUpdate = managedList.get();
            toUpdate.setName(todoList.getName());
            toUpdate.setItems(todoList.getItems());
            this.todoListRepository.save(toUpdate);
            return ResponseEntity.ok(todoListResourceAssembler.toResource(toUpdate));
        } else {
            throw new TodoListNotFoundException(listId, userId);
        }
    }

}
