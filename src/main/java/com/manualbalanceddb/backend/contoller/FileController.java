package com.manualbalanceddb.backend.contoller;
import com.manualbalanceddb.backend.model.FileMetaData;
import com.manualbalanceddb.backend.repository.FileRepository;
import com.manualbalanceddb.backend.service.MinioService;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {

    private final MinioService minioService;
    private final FileRepository fileRepository;

    public FileController(MinioService minioService,
        FileRepository fileRepository) {
        this.minioService = minioService;
        this.fileRepository = fileRepository;
        
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public FileMetaData uploadFile(
                @RequestParam("file") MultipartFile file,
                
                @RequestParam(required = false) String tags,
                @RequestParam String userId
        ) throws Exception {

            if(file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
            }

            String objectKey = minioService.uploadFile(file, tags, userId);
            String url = minioService.getFileUrl(objectKey);

            FileMetaData meta = new FileMetaData(
                    
                    file.getOriginalFilename(),
                    objectKey,
                    file.getSize(),
                    file.getContentType(),
                    url,
                    tags,
                    LocalDateTime.now(),
                    userId,
                    "default"
            );

            return fileRepository.save(meta);
        }

    @GetMapping("/user/{userId}")
    public List<FileMetaData> getFilesByUser(
            @PathVariable String userId) {

        return fileRepository.findByUserId(userId);
    }

    @PostMapping(value = "/upload-partition", consumes = "multipart/form-data")
    public FileMetaData uploadPartition(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String tags,
            @RequestParam String userId,
            @RequestParam String partition
    ) throws Exception {

        if(file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String objectKey = minioService.uploadPartition(file, tags, userId, partition);
        String url = minioService.getFileUrl(objectKey);

        FileMetaData meta = new FileMetaData(
                file.getOriginalFilename(),
                objectKey,
                file.getSize(),
                file.getContentType(),
                url,
                tags,
                LocalDateTime.now(),
                userId,
                partition
        );

        return fileRepository.save(meta);
    }

    @GetMapping("/user/{userId}/partition/{partition}")
    public List<FileMetaData> getFilesByPartition(
            @PathVariable String userId,
            @PathVariable String partition) {

        return fileRepository.findByUserIdAndPartition(
                userId,
                partition);
    }

    @DeleteMapping("/{id}")
    public void deleteFile(
            @PathVariable Long id,
            @RequestParam String userId) {

        FileMetaData file =
            fileRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND));

        if (!file.getUserId().equals(userId)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Access denied");
        }

        try {
            minioService.deleteFile(file.getObjectKey());
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Failed to delete file from MinIO"
            );
        }
        fileRepository.delete(file);
    }


    @GetMapping("/storage/{userId}")
    public Long getTotalStorageUsed(
        @PathVariable String userId) {
        return fileRepository.getStorageUsedByUser(userId);
    }

    

    
}