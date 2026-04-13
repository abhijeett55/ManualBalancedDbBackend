package com.manualbalanceddb.backend.service;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() throws Exception {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucket).build()
        );

        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucket).build()
            );
        }
    }

    public String uploadFile(MultipartFile file, String tags) throws Exception {

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        Map<String, String> metadata = new HashMap<>();
        metadata.put("name", file.getOriginalFilename());
        metadata.put("type", file.getContentType());
        metadata.put("tags", tags != null ? tags : "");
        metadata.put("size", String.valueOf(file.getSize()));
        metadata.put("uploaddate", String.valueOf(System.currentTimeMillis()));

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .userMetadata(metadata)
                        .build()
        );

        return fileName;
    }

    public String getFileUrl(String objectKey) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectKey)
                        .expiry(60 * 60 * 24)
                        .build()
        );
    }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .build()
        );
    }

    public Map<String, String> getMetadata(String objectKey) throws Exception {
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectKey)
                        .build()
        );

        return stat.userMetadata();
    }
}