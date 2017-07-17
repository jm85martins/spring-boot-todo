package com.jm85martins.todo.repositories;

import com.jm85martins.todo.entities.TodoList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by jorgemartins on 03/07/2017.
 */
@Repository
public interface TodoListRepository extends MongoRepository<TodoList,String> {
    Collection<TodoList> findByUserId(String userId);
}
