package com.sinch.countermanager.repository;

import com.sinch.countermanager.model.CounterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounterValueRepository extends JpaRepository<CounterEntity, Integer> {
}
