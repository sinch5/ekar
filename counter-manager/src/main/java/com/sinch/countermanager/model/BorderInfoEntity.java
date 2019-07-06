package com.sinch.countermanager.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BorderInfoEntity {

    @Id
    private Integer id;

    private LocalDateTime timestamp;

    private Integer value;

    public BorderInfoEntity() {
    }

    public BorderInfoEntity(Integer value, Integer id) {
        this.value = value;
        this.id = id;
        timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Integer getValue() {
        return value;
    }
}
