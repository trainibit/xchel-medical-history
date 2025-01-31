package com.trainibit.xchel.medical_history.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "chronic_diseases")
public class ChronicDisease {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_records_id_gen")
    @SequenceGenerator(name = "medical_records_id_gen", sequenceName = "medical_records_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDate createdDate;

    @NotNull
    @Column(name = "updated_date", nullable = false)
    @UpdateTimestamp
    private LocalDate updatedDate;

    @NotNull
    @Column(name = "patient_uuid", nullable = false)
    private UUID patientUuid;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active = false;
}