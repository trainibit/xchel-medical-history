package com.trainibit.xchel.medical_history.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "diseases_by_clinical_history")
public class DiseasesByClinicalHistory{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonBackReference
    @JoinColumn(name = "clinical_history_id", nullable = false)
    private MedicalHistory medicalHistory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chronic_disease_id", nullable = false)
    private ChronicDisease chronicDisease;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_date", nullable = false)
    private Instant updatedDate;

    @Column(name = "active", nullable = false)
    private Boolean active = false;
}