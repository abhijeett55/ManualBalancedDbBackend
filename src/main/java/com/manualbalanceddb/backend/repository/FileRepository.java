package com.manualbalanceddb.backend.repository;

import com.manualbalanceddb.backend.model.FileMetaData;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface FileRepository
        extends JpaRepository<FileMetaData, Long> {

    List<FileMetaData> findByUserId(String userId);

    List<FileMetaData> findByUserIdAndPartition(
            String userId,
            String partition);

    Long countByUserIdAndPartition(
            String userId,
            String partition);

    @Query("""
        SELECT COALESCE(SUM(f.size),0)
        FROM FileMetaData f
        WHERE f.userId = :userId
        AND f.partition = :partition
    """)
    Long getPartitionStorage(
            @Param("userId") String userId,
            @Param("partition") String partition);

    @Query("""
        SELECT COALESCE(SUM(f.size),0)
        FROM FileMetaData f
        WHERE f.userId = :userId
    """)
    Long getStorageUsedByUser(
            @Param("userId") String userId);
}