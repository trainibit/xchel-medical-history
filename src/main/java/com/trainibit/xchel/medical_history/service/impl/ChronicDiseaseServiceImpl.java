package com.trainibit.xchel.medical_history.service.impl;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.mapper.ChronicDiseaseMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.ChronicDiseaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChronicDiseaseServiceImpl implements ChronicDiseaseService {
    final
    ChronicDiseaseRepository chronicDiseaseRepository;

    final
    ChronicDiseaseMapper chronicDiseaseMapper;

    public ChronicDiseaseServiceImpl(ChronicDiseaseRepository chronicDiseaseRepository, ChronicDiseaseMapper chronicDiseaseMapper) {
        this.chronicDiseaseRepository = chronicDiseaseRepository;
        this.chronicDiseaseMapper = chronicDiseaseMapper;
    }

    @Override
    public List<ChronicDiseaseResponse> getAllChronicDiseases() {
        return this.chronicDiseaseMapper.entityToResponseList(this.chronicDiseaseRepository.findAll());
    }

    @Override
    public ChronicDiseaseResponse getChronicDiseaseByUuid(UUID uuid) {
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.findByUuid(uuid));
    }

    @Override
    public ChronicDiseaseResponse addChronicDisease(ChronicDiseaseRequest chronicDiseaseRequest) {
        ChronicDisease newChronicDisease = this.chronicDiseaseMapper.requestToEntity(chronicDiseaseRequest);
        newChronicDisease.setActive(true);
        newChronicDisease.setUuid(UUID.randomUUID());
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.save(newChronicDisease));
    }

    @Override
    public ChronicDiseaseResponse deleteChronicDisease(UUID uuid) {
        ChronicDisease chronicDiseaseToDelete = this.chronicDiseaseRepository.findByUuid(uuid);
        chronicDiseaseToDelete.setActive(false);
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.save(chronicDiseaseToDelete));
    }

    @Override
    public ChronicDiseaseResponse updateChronicDisease(UUID uuid, ChronicDiseaseRequest chronicDiseaseRequest) {
        ChronicDisease chronicDiseaseToUpdate = this.chronicDiseaseRepository.findByUuid(uuid);
        chronicDiseaseToUpdate.setName(chronicDiseaseRequest.getName() == null ? chronicDiseaseToUpdate.getName() : chronicDiseaseRequest.getName());
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.save(chronicDiseaseToUpdate));
    }
}
