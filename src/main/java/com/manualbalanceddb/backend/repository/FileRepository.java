package com.manualbalanceddb.backend.repository;

import com.manualbalanceddb.backend.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
}