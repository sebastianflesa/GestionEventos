package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Role {
    @Id @GeneratedValue
    private Long id;
    private String name;

    public void setName(String string) {
        this.name = string;
    }
    public String getName() {
        return name;
    }
}