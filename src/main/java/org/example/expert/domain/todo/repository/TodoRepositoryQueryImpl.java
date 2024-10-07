package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.query.TodoSearchQueryDto;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
@Repository
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        Todo result = jpaQueryFactory
                .selectFrom(todo)
                .leftJoin(todo.user)
                .where(todo.id.eq(todoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Page<TodoSearchQueryDto> searchTodos(
            String titleKeyword,
            String managerNickname,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        List<TodoSearchQueryDto> results = jpaQueryFactory
                .select(Projections.constructor(
                        TodoSearchQueryDto.class,
                        todo.title,
                        manager.countDistinct(),
                        comment.count()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(titleKeyword),
                        managerNicknameContains(managerNickname),
                        createdAtBetween(startDate, endDate)
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(todo.count())
                        .from(todo)
                        .where(
                                titleContains(titleKeyword),
                                managerNicknameContains(managerNickname),
                                createdAtBetween(startDate, endDate)
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression titleContains(String titleKeyword) {
        return StringUtils.hasText(titleKeyword)
                ? QTodo.todo.title.contains(titleKeyword)
                : null;
    }

    private BooleanExpression managerNicknameContains(String nickname) {
        return StringUtils.hasText(nickname)
                ? QUser.user.nickname.contains(nickname)
                : null;
    }

    private BooleanExpression createdAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return QTodo.todo.createdAt.between(startDate, endDate);
        } else if (startDate != null) {
            return QTodo.todo.createdAt.goe(startDate);
        } else if (endDate != null) {
            return QTodo.todo.createdAt.loe(endDate);
        } else {
            return null;
        }
    }
}
