package com.sinch.countermanager.services.impl;

import com.sinch.countermanager.model.BorderInfoEntity;
import com.sinch.countermanager.repository.BorderInfoRepository;
import com.sinch.countermanager.services.BorderInfoService;
import org.springframework.stereotype.Service;

/**
 * @author Sinchinov Yury
 * Service to store timestamps when border is reached
 */
@Service("borderInfoService")
public class BorderInfoServiceImpl implements BorderInfoService {

    private final BorderInfoRepository borderInfoRepository;

    public BorderInfoServiceImpl(BorderInfoRepository borderInfoRepository) {
        this.borderInfoRepository = borderInfoRepository;
    }

    /**
     * Store event to db
     *
     * @param borderInfoEntity
     */
    @Override
    public void persist(BorderInfoEntity borderInfoEntity) {
        borderInfoRepository.save(borderInfoEntity);
    }
}
