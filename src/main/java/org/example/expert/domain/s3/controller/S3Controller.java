package org.example.expert.domain.s3.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        String fileUrl = s3Service.uploadProfileImage(authUser.getId(), file);
        return ResponseEntity.ok(fileUrl);
    }
}
