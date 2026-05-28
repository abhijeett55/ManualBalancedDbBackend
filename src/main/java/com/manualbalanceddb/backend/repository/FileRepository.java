package com.manualbalanceddb.backend.repository;

import com.manualbalanceddb.backend.model.FileMetaData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
    List<FileMetaData> findByUserId(String userId);
}