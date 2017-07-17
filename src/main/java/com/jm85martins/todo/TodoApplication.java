package com.jm85martins.todo;

import com.jm85martins.todo.entities.TodoList;
import com.jm85martins.todo.repositories.TodoListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }

    @Bean
    CommandLineRunner init(TodoListRepository todoListRepository) {
        return (evt) -> Arrays.asList(
                "todo list 1,todo list 2,todo list 3,todo list 4,todo list 5".split(","))
                .forEach(
                        a -> {
                            todoListRepository.save(TodoList.builder().name(a).userId("123").build());
                        }
                );
    }
}
