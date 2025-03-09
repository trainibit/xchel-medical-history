package com.trainibit.xchel.medical_history.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CHRONIC_DISEASES")

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
public class ChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "UPDATED_DATE", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private Character active = 'Y';
}
