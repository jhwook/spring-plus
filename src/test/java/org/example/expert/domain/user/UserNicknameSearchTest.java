package org.example.expert.domain.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserNicknameSearchTest {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;  // EntityManager 주입

    @Test
    @Transactional
    public void generateUsers() {
        String targetNickname = "targetUser";
        int batchSize = 10_000;

        for (int i = 0; i < 1_000_000; i++) {
            String nickname;
            if (i == 999_999) {
                nickname = targetNickname;
            } else {
                nickname = UUID.randomUUID().toString().substring(0, 8);
            }
            User user = new User();
            user.updateUserNickname(nickname);
            userRepository.save(user);

            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        long startTime = System.currentTimeMillis();

        Optional<User> user = userRepository.findByNickname(targetNickname);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull(user);
        assertEquals(targetNickname, user.orElseThrow().getNickname());

        System.out.println("Time taken to search: " + duration + " ms");
    }
}

