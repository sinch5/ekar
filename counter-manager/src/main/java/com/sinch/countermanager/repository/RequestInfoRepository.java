package com.sinch.countermanager.repository;

import com.sinch.countermanager.model.RequestInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestInfoRepository extends JpaRepository<RequestInfoEntity, Long> {
}
