package com.snowstore.terra.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.snowstore.terra.entity.PropertyKey;
import com.snowstore.terra.entity.SystemVersion;

public interface PropertyKeyDao extends CrudRepository<PropertyKey, Long> {

	PropertyKey findBySystemVersionAndPropertyKeyAndStatus(SystemVersion systemVersion, String propertyKey,String status);
	
	List<PropertyKey> findBySystemVersionAndPropertyKey(SystemVersion systemVersion, String propertyKey);
}
