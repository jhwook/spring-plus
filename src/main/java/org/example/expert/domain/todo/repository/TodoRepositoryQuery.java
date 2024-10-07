package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.query.TodoSearchQueryDto;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TodoRepositoryQuery {
    Optional<Todo> findByIdWithUser(Long todoId);

    Page<TodoSearchQueryDto> searchTodos(
            String titleKeyword,
            String managerNickname,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
