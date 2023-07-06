package com.example.order.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OUTBOX")
public class OutBoxEntity {

    @Id
    @Column(name = "uuid")
    private UUID uuid;
    private Integer aggregateId;
    private String eventType;
    private String payload;
    private Date createdOn;
}
