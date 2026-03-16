package com.manualbalanceddb.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.manualbalanceddb.backend.model.FileMetaData;
import org.springframework.stereotype.Repository;
public interface FileMetaDataRepository extends JpaRepository<FileMetaData , Long> {

}