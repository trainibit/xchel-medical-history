package com.trainibit.xchel.medical_history.service.impl;

import com.trainibit.xchel.medical_history.entity.ChronicDisease;
import com.trainibit.xchel.medical_history.mapper.ChronicDiseaseMapper;
import com.trainibit.xchel.medical_history.repository.ChronicDiseaseRepository;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.ChronicDiseaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    @Cacheable(value = "chronicDiseases", key = "'all'")
    public List<ChronicDiseaseResponse> getAllChronicDiseases() {
        log.info("Obteniendo las enfermedades crónicas desde la Base de Datos");
        return this.chronicDiseaseMapper.entityToResponseList(this.chronicDiseaseRepository.findAll());
    }

    @Override
    @Cacheable(value = "chronicDisease", key="#uuid")
    public ChronicDiseaseResponse getChronicDiseaseByUuid(UUID uuid) {
        log.info("Obteniendo la enfermedad crónica desde la Base de Datos");
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
    @CacheEvict(cacheNames = "chronicDisease", key = "#uuid", beforeInvocation = true)
    public ChronicDiseaseResponse deleteChronicDisease(UUID uuid) {
        ChronicDisease chronicDiseaseToDelete = this.chronicDiseaseRepository.findByUuid(uuid);
        chronicDiseaseToDelete.setActive(false);
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.save(chronicDiseaseToDelete));
    }

    @Override
    @CachePut(cacheNames = "chronicDisease", key="#uuid")
    public ChronicDiseaseResponse updateChronicDisease(UUID uuid, ChronicDiseaseRequest chronicDiseaseRequest) {
        ChronicDisease chronicDiseaseToUpdate = this.chronicDiseaseRepository.findByUuid(uuid);
        chronicDiseaseToUpdate.setName(chronicDiseaseRequest.getName() == null ? chronicDiseaseToUpdate.getName() : chronicDiseaseRequest.getName());
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseRepository.save(chronicDiseaseToUpdate));
    }
}
