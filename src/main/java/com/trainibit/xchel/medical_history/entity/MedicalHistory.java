package com.trainibit.xchel.medical_history.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "medical_records")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "allergies", nullable = false, length = Integer.MAX_VALUE)
    private String allergies;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "size", nullable = false)
    private Float size;

    @Column(name = "blood_pressure", length = Integer.MAX_VALUE)
    private String bloodPressure;

    @Column(name = "heart_rate", length = Integer.MAX_VALUE)
    private String heartRate;

    @Column(name = "last_medical_prescription_uuid", nullable = false)
    private Long lastMedicalPrescriptionUuid;

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "updated_date", nullable = false)
    @UpdateTimestamp
    private Timestamp updatedDate;

    @Column(name = "patient_uuid", nullable = false)
    private UUID patientUuid;

    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @JsonManagedReference
    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DiseasesByClinicalHistory> chronicDiseases = new ArrayList<>();
}