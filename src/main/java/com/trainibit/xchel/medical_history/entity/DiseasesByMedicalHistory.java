package com.trainibit.xchel.medical_history.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DISEASES_BY_CLINICAL_HISTORY")

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DiseasesByMedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonBackReference
    @JoinColumn(name = "CLINICAL_HISTORY_ID", nullable = false)
    private MedicalHistory medicalHistory;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CHRONIC_DISEASE_ID", nullable = false)
    private ChronicDisease chronicDisease;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "UPDATED_DATE", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "ACTIVE", nullable = false)
    private Character active;
}
