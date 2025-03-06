package com.trainibit.xchel.medical_history.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_records")

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "allergies", nullable = false)
    private String allergies;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "size", nullable = false)
    private Float size;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "heart_rate_bpm")
    private Integer heartRateBpm;

    @Column(name = "last_medical_prescription_uuid", nullable = false)
    private UUID lastMedicalPrescriptionUuid;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "updated_date", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "patient_uuid", nullable = false)
    private UUID patientUuid;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @JsonManagedReference
    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DiseasesByMedicalHistory> chronicDiseases = new ArrayList<>();
}
