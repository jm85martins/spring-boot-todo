package com.jm85martins.todo.exceptions;

import com.jm85martins.todo.controllers.TodoListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jorgemartins on 19/07/2017.
 */
public abstract class BusinessException extends RuntimeException {
    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String developerMessage;
    private String userMessage;

    public BusinessException(String userMessage){
        super(userMessage);
        this.developerMessage = userMessage;
        this.userMessage = userMessage;
        this.log();
    }

    public BusinessException(String userMessage, String developerMessage){
        super(userMessage);
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
        this.log();
    }

    private void log(){
        this.logger.error(this.developerMessage);
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }
}
