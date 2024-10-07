package org.example.expert.domain.todo.dto.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TodoSearchQueryDto {
    // 일정 제목
    private String title;
    // 담당자 수
    private Long managerCount;
    // 댓글 수
    private Long commentCount;

    public TodoSearchQueryDto(String title, Long managerCount, Long commentCount) {
        this.title = title;
        this.managerCount = managerCount;
        this.commentCount = commentCount;
    }
}
