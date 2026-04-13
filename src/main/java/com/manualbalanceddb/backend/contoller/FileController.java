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
                @RequestParam(required = false) String tags
        ) throws Exception {

            if(file.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
            }

            String objectKey = minioService.uploadFile(file, tags);
            String url = minioService.getFileUrl(objectKey);

            FileMetaData meta = new FileMetaData(
                    file.getOriginalFilename(),
                    objectKey,
                    file.getSize(),
                    file.getContentType(),
                    url,
                    tags,
                    LocalDateTime.now()
            );

            return fileRepository.save(meta);
        }

    @GetMapping
    public List<FileMetaData> getFiles() {
        return fileRepository.findAll();
    }
    
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {

        FileMetaData file = fileRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found"));

        try {
            minioService.deleteFile(file.getObjectKey());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "MinIO delete failed");
        }

        fileRepository.delete(file);
    }
}