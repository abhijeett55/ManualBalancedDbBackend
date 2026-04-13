package com.manualbalanceddb.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String objectKey;
    private long size;
    private String type;
    @Lob
    private String url;
    private String tags;
    private LocalDateTime uploadDate;

    public FileMetaData() {}

    public FileMetaData(String name, String objectKey, long size, String type, String url, String tags, LocalDateTime uploadDate) {
        this.name = name;
        this.objectKey = objectKey;
        this.size = size;
        this.type = type;
        this.url = url;
        this.tags = tags;
        this.uploadDate = uploadDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getobjectKey() {
        return objectKey;
    }

    public long getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getTags() {
        return tags;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setobjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
