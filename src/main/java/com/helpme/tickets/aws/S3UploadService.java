package com.helpme.tickets.aws;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;

    public void uploadFile(MultipartFile file, String bucketName, String key) throws IOException {
        Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, tempFile);
        Files.delete(tempFile);
    }

    public void uploadFile(MultipartFile file, String bucketName, String key, Map<String, String> metadata) throws IOException {
        Path tempFile = Files.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .metadata(metadata)
                .build();

        s3Client.putObject(putObjectRequest, tempFile);
        Files.delete(tempFile);
    }
}
