package com.trainibit.xchel.medical_history.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "medical_records")
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_records_id_gen")
    @SequenceGenerator(name = "medical_records_id_gen", sequenceName = "medical_records_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @NotNull
    @Column(name = "allergies", nullable = false, length = Integer.MAX_VALUE)
    private String allergies;

    @Column(name = "weight")
    private Float weight;

    @NotNull
    @Column(name = "size", nullable = false)
    private Float size;

    @Column(name = "blood_pressure", length = Integer.MAX_VALUE)
    private String bloodPressure;

    @Column(name = "heart_rate", length = Integer.MAX_VALUE)
    private String heartRate;

    @NotNull
    @Column(name = "last_medical_prescription_uuid", nullable = false)
    private Long lastMedicalPrescriptionUuid;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "medical-records", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChronicDisease> chronicDiseases = new ArrayList<>();
}