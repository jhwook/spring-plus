package org.example.expert.domain.s3.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Transactional(readOnly = true)
public class S3Service {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private final UserRepository userRepository;

    public S3Service(
            @Value("${aws.access.key.id}") String accessKeyId,
            @Value("${aws.secret.access.key}") String secretAccessKey,
            @Value("${aws.region}") String region,
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }


    @Transactional
    public String uploadProfileImage(Long userId, MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        String key = "profile-images/" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket("spring-expert-bucket")
                .key(key)
                .contentType("image/png")
                .build();

        s3Client.putObject(putObjectRequest, tempFile);

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        User user = userRepository.findById(userId).orElseThrow();
        user.updateImgUrl(fileUrl);
        userRepository.save(user);

        return fileUrl;
    }
}
