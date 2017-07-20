package com.jm85martins.todo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jorgemartins on 19/07/2017.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoListNotFoundException extends BusinessException {
    private static final String developerMessageTemplate = "Todo list with id %s for user %s not found.";
    private static final String userMessageTemplate = "Todo List not found for the given id.";

    public TodoListNotFoundException(String listId, String userId) {
        super(userMessageTemplate, String.format(developerMessageTemplate, listId, userId));
    }
}
