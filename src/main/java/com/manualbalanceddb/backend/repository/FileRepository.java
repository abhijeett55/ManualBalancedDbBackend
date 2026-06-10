package com.manualbalanceddb.backend.repository;

import com.manualbalanceddb.backend.model.FileMetaData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileRepository extends JpaRepository<FileMetaData, Long> {
    List<FileMetaData> findByUserId(String userId);
    List<FileMetaData> findByUserIdAndPartition(String userId, String Partition);
    
    @Query("SELECT COALESCE(SUM(f.size), 0) FROM FileMetaData f WHERE f.userId = :userId")
    Long getStorageUsedByUser(String userId);


    
}