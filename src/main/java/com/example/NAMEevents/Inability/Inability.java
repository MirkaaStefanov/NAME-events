package com.example.NAMEevents.Inability;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Inability {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotEmpty
    private String inability;

    public String getInability() {
        return inability;
    }

    public void setInability(String inability) {
        this.inability = inability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
