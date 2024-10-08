package org.example.expert.domain.manager.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.manager.entity.SaveManagerLog;
import org.example.expert.domain.manager.repository.SaveManagerLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerLogService {
    private final SaveManagerLogRepository saveManagerLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveManagerLog(String details, Boolean isSaved, Long userId, Long todoId) {
        System.out.println("로그 생성 호출");
        System.out.println("==========   " + details + "  " + isSaved + "  " + userId + "  " + todoId + "   ==========");
        SaveManagerLog saveManagerLog = new SaveManagerLog(details, isSaved, userId, todoId);
        saveManagerLogRepository.save(saveManagerLog);
    }
}
