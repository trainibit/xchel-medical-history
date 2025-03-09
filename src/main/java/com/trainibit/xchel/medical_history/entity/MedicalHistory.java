package com.trainibit.xchel.medical_history.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "MEDICAL_RECORDS")

@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "ALLERGIES", nullable = false)
    private String allergies;

    @Column(name = "WEIGHT")
    private Float weight;

    @Column(name = "HEIGHT", nullable = false)
    private Float height;

    @Column(name = "BLOOD_PRESSURE")
    private String bloodPressure;

    @Column(name = "HEART_RATE_BPM")
    private Integer heartRateBpm;

    @Column(name = "LAST_MEDICAL_PRESCRIPTION_UUID", nullable = false)
    private String lastMedicalPrescriptionUuid;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "UPDATED_DATE", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "PATIENT_UUID", nullable = false)
    private String patientUuid;

    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private Character active = 'Y';

    @JsonManagedReference
    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DiseasesByMedicalHistory> chronicDiseases = new ArrayList<>();
}
