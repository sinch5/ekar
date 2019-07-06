package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.RequestInfoEntity;
import com.sinch.countermanager.repository.RequestInfoRepository;
import com.sinch.countermanager.services.RequestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestInfoServiceImpl implements RequestInfoService {

    private final RequestInfoRepository requestInfoRepository;

    @Autowired
    public RequestInfoServiceImpl(RequestInfoRepository requestInfoRepository) {
        this.requestInfoRepository = requestInfoRepository;
    }

    @Override
    public void persist(RequestInfoEntity requestInfoEntity) {
        requestInfoRepository.save(requestInfoEntity);
    }
}
