package com.trainibit.xchel.medical_history.service.jdbc.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.trainibit.xchel.medical_history.dao.ChronicDiseaseDao;
import com.trainibit.xchel.medical_history.mapper.ChronicDiseaseMapper;
import com.trainibit.xchel.medical_history.request.ChronicDiseaseRequest;
import com.trainibit.xchel.medical_history.response.ChronicDiseaseResponse;
import com.trainibit.xchel.medical_history.service.jdbc.ChronicDiseaseServiceJdbc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChronicDiseaseServiceJdbcImpl implements ChronicDiseaseServiceJdbc {

    final ChronicDiseaseMapper chronicDiseaseMapper;

    final ChronicDiseaseDao chronicDiseaseDao;

    public ChronicDiseaseServiceJdbcImpl(ChronicDiseaseMapper chronicDiseaseMapper,
            ChronicDiseaseDao chronicDiseaseDao) {
        this.chronicDiseaseMapper = chronicDiseaseMapper;
        this.chronicDiseaseDao = chronicDiseaseDao;
    }

    @Override
    @Cacheable(value = "chronicDiseases", key = "'all'")
    public List<ChronicDiseaseResponse> getAllChronicDiseases() {
        log.info("Obteniendo las enfermedades crónicas desde la Base de Datos con JDBC");
        return this.chronicDiseaseMapper.entityToResponseList(this.chronicDiseaseDao.getAllChronicDiseases());
    }

    @Override
    @Cacheable(value = "chronicDisease", key = "#uuid")
    public ChronicDiseaseResponse getChronicDiseaseByUuid(String uuid) {
        log.info("Obteniendo la enfermedad crónica desde la Base de Datos con JDBC");
        return this.chronicDiseaseMapper.entityToResponse(this.chronicDiseaseDao.getChronicDiseaseByUuid(uuid));
    }

    @Override
    @CacheEvict(cacheNames = "chronicDiseases", key = "'all'", beforeInvocation = true)
    public ChronicDiseaseResponse addChronicDisease(ChronicDiseaseRequest chronicDiseaseRequest) {
        return this.chronicDiseaseMapper
                .entityToResponse(this.chronicDiseaseDao.saveChronicDisease(chronicDiseaseRequest.getName()));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "chronicDisease", key = "#uuid", beforeInvocation = true),
            @CacheEvict(cacheNames = "chronicDiseases", key = "'all'", beforeInvocation = true)
    })
    public void deleteChronicDisease(String uuid) {
        this.chronicDiseaseDao.deleteChronicDisease(uuid);
    }

    @Override
    @CachePut(cacheNames = "chronicDisease", key = "#uuid")
    @CacheEvict(cacheNames = "chronicDiseases", key = "'all'", beforeInvocation = true)
    public ChronicDiseaseResponse updateChronicDisease(String uuid, ChronicDiseaseRequest chronicDiseaseRequest) {
        return this.chronicDiseaseMapper
                .entityToResponse(this.chronicDiseaseDao.editChronicDisease(uuid, chronicDiseaseRequest.getName()));
    }
}
