package com.manualbalanceddb.backend.contoller;

import com.manualbalanceddb.backend.model.FileMetaData;
import com.manualbalanceddb.backend.repository.FileRepository;
import com.manualbalanceddb.backend.service.MinioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

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

        String url = minioService.uploadFile(file);

        FileMetaData meta = new FileMetaData(
                file.getOriginalFilename(),
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType(),
                url,
                tags,
                LocalDateTime.now()
        );

        return fileRepository.save(meta);
    }
}