package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepositoryQuery {
    Optional<Todo> findByIdWithUser(Long todoId);
}
