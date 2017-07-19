package com.jm85martins.todo.repositories;

import com.jm85martins.todo.entities.TodoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by jorgemartins on 03/07/2017.
 */
@Repository
public interface TodoListRepository extends MongoRepository<TodoList,String> {
    Page<TodoList> findByUserId(String userId, Pageable pageable);

    Optional<TodoList> findByIdAndUserId(String id, String userId);
}
