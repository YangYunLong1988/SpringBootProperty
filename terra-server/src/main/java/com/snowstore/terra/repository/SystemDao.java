package com.snowstore.terra.repository;

import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.System;

public interface SystemDao extends CrudRepository<System, Long> {

	System findOneBySystemCode(String systemCode);
}
