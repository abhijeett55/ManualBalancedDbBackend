package com.manualbalanceddb.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartitionResponse {
    private Long id;
    private String name;
    private String icon;
    private String status;

    private Long usedSpace;
    private Long fileCount;
    private Double percentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}