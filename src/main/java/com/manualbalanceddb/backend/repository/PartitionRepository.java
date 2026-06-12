package com.manualbalanceddb.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.manualbalanceddb.backend.model.Partition;

@Repository
public interface PartitionRepository extends JpaRepository<Partition, Long> {
    List<Partition> findByUserId(String userId);
    @Query("""
SELECT COALESCE(SUM(f.size),0)
FROM FileMetaData f
WHERE f.userId = :userId
AND f.partition = :partition
""")
Long getPartitionStorage(
        String userId,
        String partition
);
    
}