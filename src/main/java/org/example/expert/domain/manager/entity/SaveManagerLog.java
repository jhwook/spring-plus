package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "log")
public class SaveManagerLog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_saved")
    private Boolean isSaved = false;

    @Column(name = "details")
    private String details;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "todo_id")
    private Long todoId;


    public SaveManagerLog(String details, Boolean isSaved, Long userId, Long todoId) {
        this.details = details;
        this.isSaved = isSaved;
        this.userId = userId;
        this.todoId = todoId;
    }
}
