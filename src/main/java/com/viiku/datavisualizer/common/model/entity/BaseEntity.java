package com.viiku.datavisualizer.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Abstract base class named {@link BaseEntity} for entities,
 * providing created and updated timestamps.
 */

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {

//    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

//    @UpdateTimestamp
    @Column(name = "UPDATED_AT", updatable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        this.createdAt = Optional.ofNullable(this.createdAt)
                .orElse(LocalDateTime.now());

        this.updatedAt = Optional.ofNullable(this.updatedAt)
                .orElse(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}