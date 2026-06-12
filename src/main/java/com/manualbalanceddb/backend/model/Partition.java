package com.manualbalanceddb.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "partitions")
public class Partition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String name;
    private String icon;
    private String status;
    public Partition() { }

    public Partition(String userId, String name,String icon, String status) {
        this.userId = userId;
        this.name = name;
        this.icon = icon;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getStatus() {
        return status;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}